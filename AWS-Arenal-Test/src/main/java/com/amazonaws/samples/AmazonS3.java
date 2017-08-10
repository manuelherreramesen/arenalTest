package com.amazonaws.samples;

public class AmazonS3 {

	private static final String SUFFIX = "/";

	public static void main(String[] args) {
		// credentials object identifying user for authentication
		// user must have AWSConnector and AmazonS3FullAccess for
		// this example to work
		AWSCredentials credentials = new BasicAWSCredentials("YourAccessKeyID", "YourSecretAccessKey");

		// create a client connection based on credentials
		AmazonS3 s3client = new AmazonS3Client(credentials);

		// create bucket - name must be unique for all S3 users
		String bucketName = "javatutorial-net-example-bucket";
		s3client.createBucket(bucketName);

		// list buckets
		for (Bucket bucket : s3client.listBuckets()) {
			System.out.println(" - " + bucket.getName());
		}

		// create folder into bucket
		String folderName = "testfolder";
		createFolder(bucketName, folderName, s3client);

		// upload file to folder and set it to public
		String fileName = folderName + SUFFIX + "testvideo.mp4";
		s3client.putObject(
				new PutObjectRequest(bucketName, fileName, new File("C:\\Users\\user\\Desktop\\testvideo.mp4"))
						.withCannedAcl(CannedAccessControlList.PublicRead));

		deleteFolder(bucketName, folderName, s3client);

		// deletes bucket
		s3client.deleteBucket(bucketName);
	}

	public static void createFolder(String bucketName, String folderName, AmazonS3 client) {
		// create meta-data for your folder and set content-length to 0
		ObjectMetadata metadata = new ObjectMetadata();
		metadata.setContentLength(0);
		// create empty content
		InputStream emptyContent = new ByteArrayInputStream(new byte[0]);
		// create a PutObjectRequest passing the folder name suffixed by /
		PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, folderName + SUFFIX, emptyContent,
				metadata);
		// send request to S3 to create folder
		client.putObject(putObjectRequest);
	}

	/**
	 * This method first deletes all the files in given folder and than the folder
	 * itself
	 */
	public static void deleteFolder(String bucketName, String folderName, AmazonS3 client) {
		List fileList = client.listObjects(bucketName, folderName).getObjectSummaries();
		for (S3ObjectSummary file : fileList) {
			client.deleteObject(bucketName, file.getKey());
		}
		client.deleteObject(bucketName, folderName);
	}

}

package com.amazonaws.service;


import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.util.List;

public final class AmazonS3Service {

    private static AWSCredentials credentials;
    private static AmazonS3 s3client;

    private static AmazonS3Service amazonS3Service = new AmazonS3Service();

    private AmazonS3Service() {

        generateCredentials();
    }

    private void generateCredentials() {
        credentials = new BasicAWSCredentials("AKIAIFP7LNFBVJXYGI5A", "H5aY37S1hebnsyIiRGH3NoZNtJToElMBfS37Qeb0");

        s3client = new AmazonS3Client(credentials);

    }


    public static AmazonS3Service getInstance() {
        return amazonS3Service;
    }

    public void createBucket(String bucketName) {
        s3client.createBucket(bucketName);
    }

    public List<Bucket> listBuckets() {
        return s3client.listBuckets();
    }

    public void createObject(String objectPath, String bucketName, String fileName) {
        s3client.putObject(
                new PutObjectRequest(bucketName, fileName, new File(objectPath))
                        .withCannedAcl(CannedAccessControlList.PublicRead));
    }

    public List<S3ObjectSummary> listObjects(String bucketName) {
        return s3client.listObjects(new ListObjectsRequest()
                .withBucketName(bucketName)).getObjectSummaries();
    }

    public String getDataFromObject(String bucketName, String key) {
        return s3client.getObject(bucketName, key).getObjectContent().getHttpRequest().getURI().toString();
    }


    public void deleteBucket(String bucketName) {
        s3client.deleteBucket(bucketName);
    }

    public void createFolder(String bucketName, String folderName, AmazonS3 client) {
        // create meta-data for your folder and set content-length to 0
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(0);
        // create empty content
        InputStream emptyContent = new ByteArrayInputStream(new byte[0]);
        // create a PutObjectRequest passing the folder name suffixed by /
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, folderName + "/", emptyContent,
                metadata);
        // send request to S3 to create folder
        client.putObject(putObjectRequest);
    }


    /**
     * This method first deletes all the files in given folder and than the folder
     * itself
     */
    public void deleteFolder(String bucketName, String folderName, AmazonS3 client) {
        List<S3ObjectSummary> fileList = client.listObjects(bucketName, folderName).getObjectSummaries();
        for (S3ObjectSummary file : fileList) {
            client.deleteObject(bucketName, file.getKey());
        }
        client.deleteObject(bucketName, folderName);
    }

}

package com.amazonaws.controller;

import com.amazonaws.service.AmazonS3Service;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class AWSController {

    @GetMapping(value = "/listBuckets", produces = "application/json")
    public List<Bucket> getBuckets() {
        return AmazonS3Service.getInstance().listBuckets();
    }

    @GetMapping(value = "/bucket/{bucketName}", produces = "application/json")
    public List<S3ObjectSummary> getObjects(@PathVariable("bucketName") String bucketName) {

        return AmazonS3Service.getInstance().listObjects(bucketName);
    }

    @GetMapping(value = "/object/{bucketName}/{key:.+}")
    public String loadObject(@PathVariable String bucketName, @PathVariable String key) {

    		return AmazonS3Service.getInstance().getDataFromObject(bucketName,key);
    }
    
    @GetMapping(value = "/createNewBucket/{bucketName}", produces = "application/json")
    public void createNewBucket(@PathVariable("bucketName") String bucketName) {
        AmazonS3Service.getInstance().createBucket(bucketName);
    }
    
    
    @GetMapping(value = "/removeBucket/{bucketName}", produces = "application/json")
    public void removeBucket(@PathVariable("bucketName") String bucketName){
        AmazonS3Service.getInstance().deleteBucket(bucketName);
    }
    
    

}
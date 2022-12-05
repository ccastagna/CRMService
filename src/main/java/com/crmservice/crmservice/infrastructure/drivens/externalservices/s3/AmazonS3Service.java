package com.crmservice.crmservice.infrastructure.drivens.externalservices.s3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;

import java.io.File;

public class AmazonS3Service implements IAmazonS3Service {

    private final AmazonS3 s3Client;
    private final String bucketName;
    private final String endpointUrl;


    public AmazonS3Service(AmazonS3 s3Client, String bucketName, String endpointUrl) {
        this.s3Client = s3Client;
        this.bucketName = bucketName;
        this.endpointUrl = endpointUrl;
    }

    @Override
    public String uploadFileTos3bucket(String fileName, File file) {
        String fileUrl = endpointUrl + "/" + bucketName + "/" + fileName;
        this.s3Client.putObject(new PutObjectRequest(bucketName, fileName, file)
                .withCannedAcl(CannedAccessControlList.PublicRead));
        return fileUrl;
    }
}

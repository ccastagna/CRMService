package com.crmservice.crmservice.infrastructure.drivens.externalservices.s3;

import java.io.File;

public interface IAmazonS3Service {
    String uploadFileTos3bucket(String photoFileName, File photoFile);
}

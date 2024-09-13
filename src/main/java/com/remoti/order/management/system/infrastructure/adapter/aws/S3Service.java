package com.remoti.order.management.system.infrastructure.adapter.aws;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.nio.file.Paths;

@Service
public class S3Service {

    private static final Logger LOGGER = LoggerFactory.getLogger(S3Service.class);
    private final S3Client s3Client;

    public S3Service(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    public void uploadOrderReceipt(String orderId, String filePath) {
        try {
            String bucketName = "your-s3-bucket-name";
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key("receipts/" + orderId + "/receipt.pdf")
                    .build();

            s3Client.putObject(putObjectRequest, RequestBody.fromFile(Paths.get(filePath)));

            LOGGER.info("Receipt uploaded successfully for the order {}", orderId);

        } catch (Exception e) {
            LOGGER.info("Error uploading file to S3: {}", e.getMessage());
        }
    }
}

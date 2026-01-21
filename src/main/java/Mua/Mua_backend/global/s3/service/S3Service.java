package Mua.Mua_backend.global.s3.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;

import java.net.URL;
import java.time.Duration;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3Service {

    private final S3Client s3Client;
    private final S3Presigner presigner;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public String createKey(String type) {
        String uuid = UUID.randomUUID().toString();

        return switch (type) {
            case "feed" -> "feeds/" + uuid + ".png";
            case "profile" -> "profiles/" + uuid + ".png";
            default -> throw new IllegalArgumentException("지원하지 않는 이미지 타입");
        };
    }

    public URL generatePreSignedUploadUrl(String key, String contentType) {
        PutObjectRequest objectRequest = PutObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .contentType(contentType)
                .build();

        PresignedPutObjectRequest presignedRequest =
                presigner.presignPutObject(builder -> builder
                        .signatureDuration(Duration.ofMinutes(5))
                        .putObjectRequest(objectRequest)
                );

        return presignedRequest.url();
    }

    public URL generatePreSignedGetUrl(String key) {
        GetObjectPresignRequest request =
                GetObjectPresignRequest.builder()
                        .signatureDuration(Duration.ofMinutes(5))
                        .getObjectRequest(r -> r
                                .bucket(bucket)
                                .key(key)
                        )
                        .build();

        return presigner.presignGetObject(request).url();
    }

    public void delete(String key) {
        if (key == null || key.isBlank()) {
            return;
        }

        DeleteObjectRequest deleteRequest =
                DeleteObjectRequest.builder()
                        .bucket(bucket)
                        .key(key)
                        .build();

        s3Client.deleteObject(deleteRequest);
    }
}

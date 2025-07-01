package util;

import java.nio.file.Path;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

public class S3Uploader {
    private static final String BUCKET_NAME = EnvLoader.get("AWS_BUCKET_NAME");
    private static final Region REGION = Region.of(EnvLoader.get("AWS_REGION")); // 東京リージョン
    private static final String ACCESS_KEY = EnvLoader.get("AWS_ACCESS_KEY");
    private static final String SECRET_KEY = EnvLoader.get("AWS_SECRET_KEY");

    private final S3Client s3;

    public S3Uploader() {
        s3 = S3Client.builder()
                .region(REGION)
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(ACCESS_KEY, SECRET_KEY)))
                .build();
    }

    public void uploadFile(Path filePath, String s3Key) {
        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(BUCKET_NAME)
                .key(s3Key)
                .build();

        s3.putObject(request, filePath);
    }
}

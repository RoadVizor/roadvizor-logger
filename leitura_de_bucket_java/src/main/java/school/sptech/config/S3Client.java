package school.sptech.config;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;

public class S3Client {
    public static software.amazon.awssdk.services.s3.S3Client createClient() {
        return software.amazon.awssdk.services.s3.S3Client.builder()
                .region(Region.US_EAST_1)
                .credentialsProvider(DefaultCredentialsProvider.create())
                .build();
    }
}

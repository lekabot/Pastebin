package edu.school21.app.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

@Service
public class YandexCloudStorageService implements RemoteStorageService {

    private final AmazonS3 s3Client;
    private final String bucketName = "mypastebinbacket";

    public YandexCloudStorageService(AmazonS3 s3Client) {
        this.s3Client = s3Client;
    }

    @Override
    public void upload(String hash, String data) {
        s3Client.putObject(bucketName, hash, data);
    }

    @Override
    public void delete(String hash) {
        s3Client.deleteObject(bucketName, hash);
    }

    @Override
    public String read(String hash) {
        S3Object s3Object = s3Client.getObject(bucketName, hash);
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(s3Object.getObjectContent(), StandardCharsets.UTF_8))) {
            return reader.lines().collect(Collectors.joining("\n"));
        } catch (IOException e) {
            throw new RuntimeException("Error reading object from Yandex Cloud Storage", e);
        }
    }
}

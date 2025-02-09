package com.fiap.video.core.service;


import com.fiap.video.config.S3Config;
import com.fiap.video.core.application.exception.VideoDownloadException;
import com.fiap.video.core.domain.VideoMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

@Service
public class S3Service {

    private final S3Config s3Config;

    @Value("${aws.s3.bucketZip}")
    private String bucketZipName;

    public S3Service(S3Config s3Config) {
        this.s3Config = s3Config;
    }

    public Path downloadFile(VideoMessage videoMessage) {
        try {
            Path tempDir = Files.createTempDirectory("secure_temp_dir");
            Path tempFile = Files.createTempFile(tempDir, "video", ".mp4");

            GetObjectRequest request = GetObjectRequest.builder()
                    .bucket(bucketZipName)
                    .key(videoMessage.getZipKeyS3())
                    .build();

            try (InputStream inputStream = s3Config.getS3Client().getObject(request);
                 OutputStream outputStream = Files.newOutputStream(tempFile, StandardOpenOption.WRITE)) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
            }

            return tempFile;
        } catch (Exception e) {
            throw new VideoDownloadException("Erro ao baixar vídeo do S3", e);
        }
    }
}


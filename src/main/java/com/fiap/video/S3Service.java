package com.fiap.video;


import com.fiap.video.config.ConfigS3;
import com.fiap.video.core.domain.VideoMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;

import java.io.*;
import java.nio.file.Files;

@Service
public class S3Service {

    private final ConfigS3 configS3;

    @Value("${aws.s3.bucketZip}")
    private String bucketVideoName;

    public S3Service(ConfigS3 configS3) {
        this.configS3 = configS3;
    }

    public File downloadFile(VideoMessage videoMessage) {
        try {
            GetObjectRequest request = GetObjectRequest.builder()
                    .bucket(bucketVideoName)
                    .key(videoMessage.getZipKeyS3())
                    .build();

            File tempFile = Files.createTempFile("video", ".mp4").toFile();
            try (InputStream inputStream = configS3.getS3Client().getObject(request);
                 FileOutputStream outputStream = new FileOutputStream(tempFile)) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
            }
            return tempFile;
        } catch (Exception e) {
            throw new RuntimeException("Erro ao baixar vídeo do S3", e);
        }
    }
}


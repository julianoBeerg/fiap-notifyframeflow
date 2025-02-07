package com.fiap.video.core.application.usecases;

import com.fiap.video.EmailService;
import com.fiap.video.FileToByteArray;
import com.fiap.video.S3Service;
import com.fiap.video.core.domain.VideoMessage;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@Service
public class CompletedEmailUseCase {

    private final S3Service s3Service;
    private final EmailService emailService;


    public CompletedEmailUseCase(S3Service s3Service, EmailService emailService) {
        this.s3Service = s3Service;
        this.emailService = emailService;
    }

    public void execute(VideoMessage videoMessage) throws IOException {
        File zipFile = s3Service.downloadFile(videoMessage);
        byte[] attachment = FileToByteArray.convertFileToBytes(zipFile);
        String subject = "Seu vídeo processado com sucesso";
        String body = "Clique no link abaixo para acessar o vídeo: " + videoMessage.getVideoUrlS3();
        emailService.sendEmail(videoMessage.getEmail(), subject, body,attachment , videoMessage.getZipKeyS3());
    }
}

package com.fiap.video.core.application.usecases;

import com.fiap.video.infraestructure.service.EmailService;
import com.fiap.video.core.utils.FileToByteArray;
import com.fiap.video.infraestructure.service.S3Service;
import com.fiap.video.core.domain.VideoMessage;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

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
        String body = """
                Olá %s,

                O frames do seu vídeo já estão gerados e você pode baixá-los no anexo deste email.
                
                Ou acessar a aba de download informando o nome do vídeo: %s
                """.formatted(videoMessage.getUser(), videoMessage.getVideoKeyS3());

        emailService.sendEmail(videoMessage.getEmail(), subject, body, attachment, videoMessage.getZipKeyS3());
    }
}

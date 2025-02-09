package com.fiap.video.application.usecases;

import com.fiap.video.core.service.EmailService;
import com.fiap.video.core.domain.VideoMessage;
import org.springframework.stereotype.Service;

@Service
public class    ErrorEmailUseCase {

    private final EmailService emailService;

    public ErrorEmailUseCase(EmailService emailService) {
        this.emailService = emailService;
    }

    public void execute(VideoMessage videoMessage) {
        String subject = "Erro ao processar vídeo";
        String body = """
                Olá %s,

                Houve um erro ao processar o vídeo: %s
                
                Tente novamente enviando um vídeo válido.
                """.formatted(videoMessage.getUser(), videoMessage.getVideoKeyS3());

        emailService.sendEmail(videoMessage.getEmail(), subject, body, null, null);
    }
}


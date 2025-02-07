package com.fiap.video.core.application.usecases;

import com.fiap.video.EmailService;
import com.fiap.video.core.domain.VideoMessage;
import org.springframework.stereotype.Service;

@Service
public class ErrorEmailUseCase {

    private final EmailService emailService;

    public ErrorEmailUseCase(EmailService emailService) {
        this.emailService = emailService;
    }

    public void execute(VideoMessage videoMessage) {
        // Envia o e-mail informando o erro
        String subject = "Erro ao processar vídeo";
        String body = "Houve um erro ao processar o vídeo. Detalhes do erro:\n\n"
                + "Chave do vídeo: " + videoMessage.getVideoKeyS3() + "\n"
                + "Por favor, verifique o sistema para mais informações.";

        // Envia o e-mail sem anexo
        emailService.sendEmail(videoMessage.getEmail(), subject, body, null, null);
    }
}


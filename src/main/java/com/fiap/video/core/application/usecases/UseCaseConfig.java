package com.fiap.video.core.application.usecases;

import com.fiap.video.infraestructure.service.EmailService;
import com.fiap.video.infraestructure.service.S3Service;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConfig {

    @Bean
    public CompletedEmailUseCase completedEmailUseCase(S3Service s3Service, EmailService emailService) {
        return new CompletedEmailUseCase(s3Service, emailService);
    }

    @Bean
    public ErrorEmailUseCase errorEmailUseCase(EmailService emailService) {
        return new ErrorEmailUseCase(emailService);
    }
}

package com.fiap.video.core.application.usecases;

import com.fiap.video.core.service.EmailService;
import com.fiap.video.core.service.S3Service;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;


@ExtendWith(SpringExtension.class)
@SpringBootTest
class UseCaseConfigTest {

    @Autowired
    private ApplicationContext context;

   @BeforeEach
   void setUp() {
      System.setProperty("aws.accessKeyId", "seuAccessKeyId");
      System.setProperty("aws.secretAccessKey", "seuSecretAccessKey");
   }


    @Test
    void shouldLoadCompletedEmailUseCaseBean() {
        CompletedEmailUseCase completedEmailUseCase = context.getBean(CompletedEmailUseCase.class);
        assertNotNull(completedEmailUseCase);
    }

    @Test
    void shouldLoadErrorEmailUseCaseBean() {
        ErrorEmailUseCase errorEmailUseCase = context.getBean(ErrorEmailUseCase.class);
        assertNotNull(errorEmailUseCase);
    }

    @Test
    void shouldLoadS3ServiceAndEmailServiceBeans() {
        S3Service s3Service = context.getBean(S3Service.class);
        EmailService emailService = context.getBean(EmailService.class);
        assertNotNull(s3Service);
        assertNotNull(emailService);
    }
}

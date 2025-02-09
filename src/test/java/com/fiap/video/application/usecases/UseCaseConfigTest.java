package com.fiap.video.application.usecases;

import com.fiap.video.application.usecases.CompletedEmailUseCase;
import com.fiap.video.application.usecases.ErrorEmailUseCase;
import com.fiap.video.core.service.EmailService;
import com.fiap.video.core.service.S3Service;
import com.fiap.video.infraestructure.config.UseCaseConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class UseCaseConfigTest {

    @Mock
    private S3Service s3Service;

    @Mock
    private EmailService emailService;

    private UseCaseConfig useCaseConfig;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        useCaseConfig = new UseCaseConfig();
    }

    @Test
    void testCompletedEmailUseCaseBeanCreation() {
        CompletedEmailUseCase completedEmailUseCase = useCaseConfig.completedEmailUseCase(s3Service, emailService);
        assertNotNull(completedEmailUseCase);
    }

    @Test
    void testErrorEmailUseCaseBeanCreation() {
        ErrorEmailUseCase errorEmailUseCase = useCaseConfig.errorEmailUseCase(emailService);
        assertNotNull(errorEmailUseCase);
    }
}

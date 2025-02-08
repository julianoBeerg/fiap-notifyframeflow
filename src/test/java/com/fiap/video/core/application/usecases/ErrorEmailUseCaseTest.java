package com.fiap.video.core.application.usecases;

import com.fiap.video.infraestructure.service.EmailService;
import com.fiap.video.core.domain.VideoMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ErrorEmailUseCaseTest {

    @Mock
    private EmailService emailService;

    @InjectMocks
    private ErrorEmailUseCase errorEmailUseCase;

    private VideoMessage videoMessage;

    @BeforeEach
    void setUp() {
        videoMessage = new VideoMessage();
        videoMessage.setEmail("user@example.com");
        videoMessage.setVideoKeyS3("video123");
        videoMessage.setUser("John Doe");
        videoMessage.setStatus("PROCESSING_ERROR");
    }

    @Test
    void shouldSendErrorEmailWhenProcessingFails() {
        errorEmailUseCase.execute(videoMessage);

        verify(emailService).sendEmail(
                videoMessage.getEmail(),
                "Erro ao processar vídeo",
                """
                Olá John Doe,

                Houve um erro ao processar o vídeo: video123
                
                Tente novamente enviando um vídeo válido.
                """,
                null,
                null
        );
    }

    @Test
    void shouldSetAndGetValuesCorrectly() {
        VideoMessage testMessage = new VideoMessage();
        testMessage.setZipKeyS3("zip123");
        testMessage.setVideoKeyS3("video123");
        testMessage.setVideoUrlS3("http://example.com/video");
        testMessage.setId("12345");
        testMessage.setUser("Test User");
        testMessage.setEmail("test@example.com");
        testMessage.setStatus("COMPLETED");

        assertEquals("zip123", testMessage.getZipKeyS3());
        assertEquals("video123", testMessage.getVideoKeyS3());
        assertEquals("http://example.com/video", testMessage.getVideoUrlS3());
        assertEquals("12345", testMessage.getId());
        assertEquals("Test User", testMessage.getUser());
        assertEquals("test@example.com", testMessage.getEmail());
        assertEquals("COMPLETED", testMessage.getStatus());
    }
}

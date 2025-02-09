package com.fiap.video.core.application.exception;
import com.fiap.video.core.application.usecases.CompletedEmailUseCase;
import com.fiap.video.core.domain.VideoMessage;
import com.fiap.video.core.service.EmailService;
import com.fiap.video.core.service.S3Service;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
class CompletedEmailUseCaseTest {

    private S3Service s3Service;
    private EmailService emailService;
    private CompletedEmailUseCase completedEmailUseCase;

    @BeforeEach
    void setUp() {
        s3Service = mock(S3Service.class);
        emailService = mock(EmailService.class);
        completedEmailUseCase = new CompletedEmailUseCase(s3Service, emailService);
    }

    @Test
    void testExecute() throws IOException {
        VideoMessage videoMessage = mock(VideoMessage.class);
        File mockFile = mock(File.class);
        when(s3Service.downloadFile(any(VideoMessage.class))).thenReturn(mockFile);
        byte[] mockBytes = new byte[]{1, 2, 3}; // Mock byte array for attachment
        try (MockedStatic<Files> mockedFiles = mockStatic(Files.class)) {
            mockedFiles.when(() -> Files.readAllBytes(any())).thenReturn(mockBytes);
            when(videoMessage.getUser()).thenReturn("John Doe");
            when(videoMessage.getVideoKeyS3()).thenReturn("video123");
            when(videoMessage.getEmail()).thenReturn("john.doe@example.com");
            when(videoMessage.getZipKeyS3()).thenReturn("zip123");
            completedEmailUseCase.execute(videoMessage);
            verify(s3Service).downloadFile(videoMessage);
            verify(emailService).sendEmail(
                    eq("john.doe@example.com"),
                    eq("Seu vídeo processado com sucesso"),
                    contains("Olá John Doe,"),
                    eq(mockBytes),
                    eq("zip123")
            );
        }
    }
}

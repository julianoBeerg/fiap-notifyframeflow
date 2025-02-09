package com.fiap.video.application.usecases;

import com.fiap.video.application.usecases.CompletedEmailUseCase;
import com.fiap.video.core.service.EmailService;
import com.fiap.video.core.utils.FileToByteArray;
import com.fiap.video.core.service.S3Service;
import com.fiap.video.core.domain.VideoMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CompletedEmailUseCaseTest {

    @Mock
    private S3Service s3Service;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private CompletedEmailUseCase completedEmailUseCase;

    private VideoMessage videoMessage;
    private File zipFile;
    private byte[] fileBytes;

    @BeforeEach
    void setUp() {
        videoMessage = new VideoMessage();
        videoMessage.setEmail("user@example.com");
        videoMessage.setVideoKeyS3("video123");
        videoMessage.setZipKeyS3("zip123");
        videoMessage.setUser("John Doe");
        videoMessage.setVideoUrlS3("https://example.com/video123");
        videoMessage.setId("id123");
        videoMessage.setStatus("COMPLETED");

        zipFile = mock(File.class);
        fileBytes = new byte[]{1, 2, 3};
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

    @Test
    void shouldSendEmailWithAttachmentWhenVideoProcessingIsCompleted() throws IOException {
        when(s3Service.downloadFile(videoMessage)).thenReturn(zipFile);

        try (MockedStatic<FileToByteArray> mockedStatic = mockStatic(FileToByteArray.class)) {
            mockedStatic.when(() -> FileToByteArray.convertFileToBytes(zipFile)).thenReturn(fileBytes);

            completedEmailUseCase.execute(videoMessage);

            verify(emailService).sendEmail(
                    eq(videoMessage.getEmail()),
                    eq("Seu vídeo processado com sucesso"),
                    contains("O frames do seu vídeo já estão gerados"),
                    eq(fileBytes),
                    eq(videoMessage.getZipKeyS3())
            );
        }
    }


}

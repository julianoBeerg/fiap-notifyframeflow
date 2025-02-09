package com.fiap.video;
import com.fiap.video.infraestructure.config.S3Config;
import com.fiap.video.application.exception.VideoDownloadException;
import com.fiap.video.core.domain.VideoMessage;
import com.fiap.video.core.service.S3Service;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AwsConfigTest {

    @Mock
    private S3Config s3Config;
    @Mock
    private S3Client s3Client;
    @InjectMocks
    private S3Service s3Service;
    private String accessKeyId;
    private String secretAccessKey;
    private String token;
    private String regionName;

    @BeforeEach
    void setUp() {
        lenient().when(s3Config.getS3Client()).thenReturn(s3Client);
        accessKeyId = "test-access-key-id";
        secretAccessKey = "test-secret-access-key";
        token = "test-token";
        regionName = "us-east-1";

        s3Config = new S3Config();
        s3Config.setAccessKeyId(accessKeyId);
        s3Config.setSecretAccessKey(secretAccessKey);
        s3Config.setToken(token);
    }

    @Test
     void testGetAccessKeyId() {
        assertEquals(accessKeyId, s3Config.getAccessKeyId(), "A chave de acesso deve ser igual.");
    }

    @Test
     void testGetSecretAccessKey() {
        assertEquals(secretAccessKey, s3Config.getSecretAccessKey(), "A chave secreta deve ser igual.");
    }

    @Test
     void testGetToken() {
        assertEquals(token, s3Config.getToken(), "O token deve ser igual.");
    }

    @Test
     void testGetRegionName() {
        assertEquals(regionName, s3Config.getRegionName(), "A região deve ser igual.");
    }

    @Test
     void testS3ClientCreation() {
        S3Client client = s3Config.getS3Client();
        assertNotNull(client, "O cliente S3 não deve ser nulo.");
    }

    @Test
    void shouldDownloadFileSuccessfully() throws Exception {
        VideoMessage videoMessage = mock(VideoMessage.class);
        when(videoMessage.getZipKeyS3()).thenReturn("test-video.mp4");
        GetObjectResponse mockResponse = mock(GetObjectResponse.class);
        InputStream fakeInputStream = new ByteArrayInputStream("fake video content".getBytes());
        ResponseInputStream<GetObjectResponse> mockResponseInputStream =
                new ResponseInputStream<>(mockResponse, fakeInputStream);
        when(s3Client.getObject(any(GetObjectRequest.class))).thenReturn(mockResponseInputStream);
        File downloadedFile = s3Service.downloadFile(videoMessage);
        assertNotNull(downloadedFile);
        assertTrue(downloadedFile.exists());
        assertTrue(downloadedFile.length() > 0);
        verify(s3Client, times(1)).getObject(any(GetObjectRequest.class));
    }

    @Test
    void shouldThrowExceptionWhenDownloadFails() {
        VideoMessage videoMessage = mock(VideoMessage.class);
        when(videoMessage.getZipKeyS3()).thenReturn("test-video.mp4");
        when(s3Client.getObject(any(GetObjectRequest.class)))
                .thenThrow(new RuntimeException("Erro ao acessar S3"));
        VideoDownloadException thrown = assertThrows(
                VideoDownloadException.class,
                () -> s3Service.downloadFile(videoMessage)
        );

        assertEquals("Erro ao baixar vídeo do S3", thrown.getMessage());
    }
}

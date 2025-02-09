package com.fiap.video.infraestructure.subscriber;
import com.fiap.video.core.application.usecases.CompletedEmailUseCase;
import com.fiap.video.core.application.usecases.ErrorEmailUseCase;
import com.fiap.video.core.domain.VideoMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.Message;

import java.io.IOException;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SQSSubscriberTest {

    @Mock
    private CompletedEmailUseCase completedEmailUseCase;

    @Mock
    private ErrorEmailUseCase errorEmailUseCase;

    @Mock
    private Message<String> sqsMessage;

    @InjectMocks
    private SQSSubscriber sqsSubscriber;

    private String completedMessage;
    private String errorMessage;

    @BeforeEach
    void setUp() {
        completedMessage = """
            {
                "Message": "{ \\"zipKeyS3\\": \\"zip123\\", \\"videoKeyS3\\": \\"video123\\", \\"videoUrlS3\\": \\"http://example.com/video\\", \\"id\\": \\"12345\\", \\"user\\": \\"Test User\\", \\"email\\": \\"test@example.com\\", \\"status\\": \\"COMPLETED\\" }"
            }
        """;

        errorMessage = """
            {
                "Message": "{ \\"zipKeyS3\\": \\"zip456\\", \\"videoKeyS3\\": \\"video456\\", \\"videoUrlS3\\": \\"http://example.com/video2\\", \\"id\\": \\"67890\\", \\"user\\": \\"Error User\\", \\"email\\": \\"error@example.com\\", \\"status\\": \\"PROCESSING_ERROR\\" }"
            }
        """;
    }

    @Test
    void shouldProcessCompletedMessage() throws IOException {
        when(sqsMessage.getPayload()).thenReturn(completedMessage);

        sqsSubscriber.receiveMessage(sqsMessage);

        verify(completedEmailUseCase).execute(any(VideoMessage.class));
        verify(errorEmailUseCase, never()).execute(any(VideoMessage.class));
    }

    @Test
    void shouldProcessErrorMessage() throws IOException {
        when(sqsMessage.getPayload()).thenReturn(errorMessage);

        sqsSubscriber.receiveMessage(sqsMessage);

        verify(errorEmailUseCase).execute(any(VideoMessage.class));
        verify(completedEmailUseCase, never()).execute(any(VideoMessage.class));
    }

    @Test
    void shouldNotProcessInvalidMessage() throws IOException {
        when(sqsMessage.getPayload()).thenReturn(null);

        sqsSubscriber.receiveMessage(sqsMessage);

        verify(completedEmailUseCase, never()).execute(any(VideoMessage.class));
        verify(errorEmailUseCase, never()).execute(any(VideoMessage.class));
    }
}

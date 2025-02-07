package com.fiap.video.infraestructure.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fiap.video.core.application.enums.VideoStatus;
import com.fiap.video.core.application.usecases.CompletedEmailUseCase;
import com.fiap.video.core.application.usecases.ErrorEmailUseCase;
import com.fiap.video.core.domain.VideoMessage;
import io.awspring.cloud.sqs.annotation.SqsListener;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SqsConsumer {

    private final ObjectMapper objectMapper;
    private final CompletedEmailUseCase completedEmailUseCase;
    private final ErrorEmailUseCase errorEmailUseCase;

    @Autowired
    public SqsConsumer(ObjectMapper objectMapper, CompletedEmailUseCase completedEmailUseCase, ErrorEmailUseCase errorEmailUseCase) {
        this.objectMapper = objectMapper;
        this.completedEmailUseCase = completedEmailUseCase;
        this.errorEmailUseCase = errorEmailUseCase;
    }

    @SqsListener("${spring.cloud.aws.sqs.queue-name}")
    public void recieveMessage(Message<String> message) {
        String content = message.getPayload();
        if (content == null) {
            log.error("Received null content from SQS");
            return;
        }

        log.info("data received ! {}", content);

        try {
            JSONObject snsMessage = new JSONObject(content);

            String messageContent = snsMessage.getString("Message");

            JSONObject videoMessageJson = new JSONObject(messageContent);

            VideoMessage videoMessage = new VideoMessage();
            videoMessage.setZipKeyS3(videoMessageJson.getString("zipKeyS3"));
            videoMessage.setVideoKeyS3(videoMessageJson.getString("videoKeyS3"));
            videoMessage.setVideoUrlS3(videoMessageJson.getString("videoUrlS3"));
            videoMessage.setId(videoMessageJson.getString("id"));
            videoMessage.setUser(videoMessageJson.getString("user"));
            videoMessage.setEmail(videoMessageJson.getString("email"));
            videoMessage.setStatus(videoMessageJson.getString("status"));

            log.info("VideoMessage: {}", videoMessage);

            if (VideoStatus.COMPLETED.toString().equals(videoMessage.getStatus())) {
                completedEmailUseCase.execute(videoMessage);
            } else if (VideoStatus.PROCESSING_ERROR.toString().equals(videoMessage.getStatus())) {
                errorEmailUseCase.execute(videoMessage);
            }

        } catch (Exception e) {
            log.error("Error processing message: {}", e.getMessage(), e);
        }
    }
}
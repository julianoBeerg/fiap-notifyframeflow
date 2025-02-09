package com.fiap.video.infraestructure.subscriber;

import com.fiap.video.application.enums.VideoStatus;
import com.fiap.video.application.usecases.CompletedEmailUseCase;
import com.fiap.video.application.usecases.ErrorEmailUseCase;
import com.fiap.video.core.domain.VideoMessage;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SQSSubscriber {

    private final CompletedEmailUseCase completedEmailUseCase;
    private final ErrorEmailUseCase errorEmailUseCase;

    @Autowired
    public SQSSubscriber(CompletedEmailUseCase completedEmailUseCase, ErrorEmailUseCase errorEmailUseCase) {
        this.completedEmailUseCase = completedEmailUseCase;
        this.errorEmailUseCase = errorEmailUseCase;
    }

    @io.awspring.cloud.sqs.annotation.SqsListener("${spring.cloud.aws.sqs.queue-name}")
    public void receiveMessage(Message<String> message) {
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
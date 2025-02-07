package com.fiap.video.core.domain;

public class SnsMessageWrapper {
    private String Type;
    private String MessageId;
    private String TopicArn;
    private String Message; // Este campo contém o JSON que você deseja processar
    private String Timestamp;
    private String UnsubscribeURL;

    // Getters e Setters
    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getMessageId() {
        return MessageId;
    }

    public void setMessageId(String messageId) {
        MessageId = messageId;
    }

    public String getTopicArn() {
        return TopicArn;
    }

    public void setTopicArn(String topicArn) {
        TopicArn = topicArn;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public String getTimestamp() {
        return Timestamp;
    }

    public void setTimestamp(String timestamp) {
        Timestamp = timestamp;
    }

    public String getUnsubscribeURL() {
        return UnsubscribeURL;
    }

    public void setUnsubscribeURL(String unsubscribeURL) {
        UnsubscribeURL = unsubscribeURL;
    }
}
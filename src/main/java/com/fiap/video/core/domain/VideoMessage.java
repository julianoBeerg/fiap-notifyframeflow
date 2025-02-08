package com.fiap.video.core.domain;

public class VideoMessage {

    private String zipKeyS3;
    private String videoKeyS3;
    private String videoUrlS3;
    private String id;
    private String user;
    private String email;
    private String status;

    public String getZipKeyS3() {
        return zipKeyS3;
    }

    public void setZipKeyS3(String zipKeyS3) {
        this.zipKeyS3 = zipKeyS3;
    }

    public String getVideoKeyS3() {
        return videoKeyS3;
    }

    public void setVideoKeyS3(String videoKeyS3) {
        this.videoKeyS3 = videoKeyS3;
    }

    public String getVideoUrlS3() {
        return videoUrlS3;
    }

    public void setVideoUrlS3(String videoUrlS3) {
        this.videoUrlS3 = videoUrlS3;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

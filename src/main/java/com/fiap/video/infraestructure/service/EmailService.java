package com.fiap.video.infraestructure.service;

import com.fiap.video.core.application.exception.EmailSendingException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.*;
import javax.mail.internet.*;
import javax.mail.util.ByteArrayDataSource;
import java.util.Properties;

@Service
public class EmailService {

    private final String smtpEmail;
    private final String smtpPassword;

    public EmailService(@Value("${smtp.email}") String smtpEmail,
                        @Value("${smtp.password}") String smtpPassword) {
        this.smtpEmail = smtpEmail;
        this.smtpPassword = smtpPassword;
    }


    public void sendEmail(String to, String subject, String body, byte[] attachment, String zipKeyS3) {
        String host = "smtp.gmail.com";
        String from = smtpEmail;
        Properties properties = new Properties();
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(smtpEmail, smtpPassword);
            }
        });

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setSubject(subject);

            MimeBodyPart textPart = new MimeBodyPart();
            textPart.setText(body);

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(textPart);

            if (attachment != null) {
                MimeBodyPart attachmentPart = new MimeBodyPart();
                DataSource source = new ByteArrayDataSource(attachment, "application/zip");
                attachmentPart.setDataHandler(new DataHandler(source));

                String fileName = zipKeyS3.substring(zipKeyS3.lastIndexOf("/") + 1);
                attachmentPart.setFileName(fileName);

                multipart.addBodyPart(attachmentPart);
            }

            message.setContent(multipart);
            Transport.send(message);
        } catch (MessagingException e) {
            throw new EmailSendingException("Erro ao enviar e-mail", e);
        }

    }
}

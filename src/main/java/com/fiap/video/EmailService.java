package com.fiap.video;

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

    @Value("${rmtp.email}")
    private String rmtpEmail;

    @Value("${rmtp.password}")
    private String rmtpPassword;


    public void sendEmail(String to, String subject, String body, byte[] attachment, String zipKeyS3) {
        String host = "smtp.gmail.com";
        String from = rmtpEmail;
        Properties properties = new Properties();
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(rmtpEmail, rmtpPassword);
            }
        });

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setSubject(subject);

            // Corpo do e-mail
            MimeBodyPart textPart = new MimeBodyPart();
            textPart.setText(body);

            // Anexo (se presente)
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(textPart);

            if (attachment != null) {
                MimeBodyPart attachmentPart = new MimeBodyPart();
                // Criando o anexo com os bytes do arquivo e o nome do arquivo baseado no zipKeyS3
                DataSource source = new ByteArrayDataSource(attachment, "application/zip");
                attachmentPart.setDataHandler(new DataHandler(source));

                // Usando o nome do arquivo como o nome do anexo (basename do zipKeyS3)
                String fileName = zipKeyS3.substring(zipKeyS3.lastIndexOf("/") + 1); // Pega o nome do arquivo
                attachmentPart.setFileName(fileName);

                multipart.addBodyPart(attachmentPart);
            }

            message.setContent(multipart);
            Transport.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Erro ao enviar e-mail", e);
        }
    }
}

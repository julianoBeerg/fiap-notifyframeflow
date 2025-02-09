package com.fiap.video.application.exception;

import com.fiap.video.application.exception.EmailSendingException;
import org.junit.jupiter.api.Test;
import javax.mail.MessagingException;

import static org.junit.jupiter.api.Assertions.assertThrows;

 class EmailSendingExceptionTest {

    @Test
    void deveLancarEmailSendingException() {
        MessagingException causa = new MessagingException("Falha na conexão SMTP");

        EmailSendingException exception =
                assertThrows(EmailSendingException.class, () -> {
                    throw new EmailSendingException("Erro ao enviar e-mail", causa);
                });

        assert exception.getMessage().contains("Erro ao enviar e-mail");

        assert exception.getCause() == causa;
    }
}

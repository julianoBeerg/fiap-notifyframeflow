package com.fiap.video.infraestructure.service;

import com.fiap.video.core.application.exception.EmailSendingException;
import com.fiap.video.core.service.EmailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.mail.MessagingException;
import javax.mail.Transport;
import javax.mail.internet.MimeMessage;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmailServiceTest {

    @InjectMocks
    private EmailService emailService;

    @BeforeEach
    void setUp() {
        emailService = new EmailService("test@gmail.com", "password123");
    }

    @Test
    void shouldSendEmailSuccessfully()  {
        try (MockedStatic<Transport> transportMock = Mockito.mockStatic(Transport.class)) {
            emailService.sendEmail("recipient@example.com", "Test Subject", "Test Body", null, null);

            // Verifica se Transport.send() foi chamado
            transportMock.verify(() -> Transport.send(any(MimeMessage.class)), times(1));
        }
    }

    @Test
    void shouldThrowExceptionWhenEmailFailsToSend() {
        try (var transportMock = mockStatic(Transport.class)) {
            transportMock.when(() -> Transport.send(any())).thenThrow(new MessagingException("Erro simulado"));

            assertThrows(EmailSendingException.class, () -> {
                emailService.sendEmail("test@example.com", "Erro Teste", "Mensagem", null, null);
            });
        }
    }

}

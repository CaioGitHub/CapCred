package com.capcredit.ms_notification.application.service;

import com.capcredit.ms_notification.application.exception.EmailSendException;
import com.capcredit.ms_notification.core.domain.Email;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mail.javamail.JavaMailSender;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class EmailServiceTest {

    private JavaMailSender mailSender;
    private EmailService emailService;
    private MimeMessage mimeMessage;

    @BeforeEach
    void setUp() {
        mailSender = mock(JavaMailSender.class);
        mimeMessage = mock(MimeMessage.class);
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
        emailService = new EmailService(mailSender);
    }

    @Test
    void deveEnviarEmailComSucesso() {
        Email email = Email.builder()
                .to("usuario@test.com")
                .subject("Assunto")
                .body("Corpo do e-mail")
                .build();

        assertDoesNotThrow(() -> emailService.send(email));
        verify(mailSender, times(1)).send(any(MimeMessage.class));
    }

    @Test
    void deveLancarExcecaoAoFalharEnvioEmail() {
        Email email = Email.builder()
                .to("usuario@test.com")
                .subject("Assunto")
                .body("Corpo do e-mail")
                .build();

        // Simula erro ao enviar email
        doThrow(new RuntimeException("Falha no envio do e-mail"))
                .when(mailSender).send(any(MimeMessage.class));

        assertThrows(EmailSendException.class, () -> emailService.send(email));
    }

}

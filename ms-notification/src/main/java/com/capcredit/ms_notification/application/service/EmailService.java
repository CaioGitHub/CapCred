package com.capcredit.ms_notification.application.service;

import com.capcredit.ms_notification.application.exception.EmailSendException;
import com.capcredit.ms_notification.core.domain.Email;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EmailService {

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    private final JavaMailSender mailSender;

    public void send(Email email) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(email.getTo());
            helper.setSubject(email.getSubject());
            helper.setText(email.getBody());

            mailSender.send(message);
        } catch (Exception e) {
            log.error("Erro ao enviar e-mail", e);
            throw new EmailSendException("Erro ao enviar e-mail", e);
        }
    }

}
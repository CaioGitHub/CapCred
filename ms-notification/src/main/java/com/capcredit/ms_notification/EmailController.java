package com.capcredit.ms_notification;


import com.capcredit.ms_notification.application.service.EmailService;
import com.capcredit.ms_notification.domain.model.Email;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/emails")
public class EmailController {

    private final EmailService emailService;

    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }


    @PostMapping("/send-email")
    public void sendTestEmail(@RequestBody Email email) {
        emailService.enviar(email);
    }
}

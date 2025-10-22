package com.capcredit.ms_notification.adapter.out;

import com.capcredit.ms_notification.application.service.EmailService;
import com.capcredit.ms_notification.application.service.SMSService;
import com.capcredit.ms_notification.core.domain.Email;
import com.capcredit.ms_notification.core.domain.SMS;
import com.capcredit.ms_notification.port.out.dtos.DeniedLoanDTO;
import com.capcredit.ms_notification.port.out.dtos.UserDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DeniedLoanNotificationImplTest {

    private EmailService emailService;
    private SMSService smsService;
    private DeniedLoanNotificationImpl deniedLoanNotification;

    @BeforeEach
    void setUp() {
        emailService = mock(EmailService.class);
        smsService = mock(SMSService.class);
        deniedLoanNotification = new DeniedLoanNotificationImpl(emailService, smsService);
    }

    @Test
    void shouldSendEmailAndSMSWhenLoanIsDenied() {
        UserDTO user = new UserDTO(
                UUID.randomUUID(),
                LocalDateTime.now(),
                UUID.randomUUID(),
                "Carlos Souza",
                "carlos@exemplo.com",
                "+5511987654321"
        );

        DeniedLoanDTO dto = new DeniedLoanDTO(
                UUID.randomUUID(),
                LocalDateTime.now(),
                UUID.randomUUID(),
                user,
                "renda insuficiente"
        );

        deniedLoanNotification.receiveDeniedLoan(dto);

        ArgumentCaptor<Email> emailCaptor = ArgumentCaptor.forClass(Email.class);
        verify(emailService, times(1)).send(emailCaptor.capture());
        Email sentEmail = emailCaptor.getValue();

        assertEquals("carlos@exemplo.com", sentEmail.getTo());
        assertEquals("Seu empréstimo foi negado!", sentEmail.getSubject());
        assertTrue(sentEmail.getBody().contains("Carlos Souza"));
        assertTrue(sentEmail.getBody().contains("renda insuficiente"));

        ArgumentCaptor<SMS> smsCaptor = ArgumentCaptor.forClass(SMS.class);
        verify(smsService, times(1)).send(smsCaptor.capture());
        SMS sentSms = smsCaptor.getValue();

        assertEquals("+5511987654321", sentSms.getToNumber());
        assertTrue(sentSms.getMessage().contains("Carlos Souza"));
        assertTrue(sentSms.getMessage().contains("renda insuficiente"));
    }
}

package com.capcredit.ms_notification.adapter.out;

import com.capcredit.ms_notification.application.service.EmailService;
import com.capcredit.ms_notification.application.service.SMSService;
import com.capcredit.ms_notification.core.domain.Email;
import com.capcredit.ms_notification.core.domain.SMS;
import com.capcredit.ms_notification.port.out.dtos.PaymentCompletedDTO;
import com.capcredit.ms_notification.port.out.dtos.UserDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PaymentCompletedNotificationImplTest {

    private EmailService emailService;
    private SMSService smsService;
    private PaymentCompletedNotificationImpl paymentCompletedNotification;

    @BeforeEach
    void setUp() {
        emailService = mock(EmailService.class);
        smsService = mock(SMSService.class);
        paymentCompletedNotification = new PaymentCompletedNotificationImpl(emailService, smsService);
    }

    @Test
    void shouldSendEmailAndSMSWhenPaymentIsCompleted() {
        // Arrange
        UserDTO user = new UserDTO(
                UUID.randomUUID(),
                LocalDateTime.now(),
                UUID.randomUUID(),
                "Ana Pereira",
                "ana@exemplo.com",
                "+5511988776655"
        );

        PaymentCompletedDTO dto = new PaymentCompletedDTO(
                UUID.randomUUID(),
                LocalDateTime.now(),
                UUID.randomUUID(),
                user,
                LocalDate.now()
        );

        // Act
        paymentCompletedNotification.receiveCompletedPayment(dto);

        // Assert Email
        ArgumentCaptor<Email> emailCaptor = ArgumentCaptor.forClass(Email.class);
        verify(emailService, times(1)).send(emailCaptor.capture());
        Email sentEmail = emailCaptor.getValue();

        assertEquals("ana@exemplo.com", sentEmail.getTo());
        assertEquals("Parabéns! Você quitou seu empréstimo!", sentEmail.getSubject());
        assertTrue(sentEmail.getBody().contains(dto.loanId().toString()));
        assertTrue(sentEmail.getBody().contains(dto.finalizationDate().toString()));

        // Assert SMS
        ArgumentCaptor<SMS> smsCaptor = ArgumentCaptor.forClass(SMS.class);
        verify(smsService, times(1)).send(smsCaptor.capture());
        SMS sentSms = smsCaptor.getValue();

        assertEquals("+5511988776655", sentSms.getToNumber());
        assertTrue(sentSms.getMessage().contains(dto.loanId().toString()));
        assertTrue(sentSms.getMessage().contains(dto.finalizationDate().toString()));
    }
}

//package com.capcredit.ms_notification.adapter.out;
//
//import com.capcredit.ms_notification.application.service.EmailService;
//import com.capcredit.ms_notification.application.service.SMSService;
//import com.capcredit.ms_notification.core.domain.Email;
//import com.capcredit.ms_notification.core.domain.SMS;
//import com.capcredit.ms_notification.port.out.dtos.PaymentReceivedDTO;
//import com.capcredit.ms_notification.port.out.dtos.UserDTO;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.ArgumentCaptor;
//
//import java.math.BigDecimal;
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.util.UUID;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertTrue;
//import static org.mockito.Mockito.*;
//
//class PaymentReceivedNotificationImplTest {
//
//    private EmailService emailService;
//    private SMSService smsService;
//    private PaymentReceivedNotificationImpl paymentReceivedNotification;
//
//    @BeforeEach
//    void setUp() {
//        emailService = mock(EmailService.class);
//        smsService = mock(SMSService.class);
//        paymentReceivedNotification = new PaymentReceivedNotificationImpl(emailService, smsService);
//    }
//
//    @Test
//    void shouldSendEmailAndSMSWhenPaymentIsReceived() {
//        UserDTO user = new UserDTO(null, null, UUID.randomUUID(), "Carlos Eduardo", "carlos@exemplo.com", "+5511988776655");
//
//        PaymentReceivedDTO dto = new PaymentReceivedDTO(
//                UUID.randomUUID(),
//                UUID.randomUUID(),
//                5,
//                new BigDecimal("1500.00"),
//                LocalDate.now().plusDays(5),
//                LocalDateTime.now(),
//                new BigDecimal("1500.00"),
//                "PAID",
//                user
//        );
//
//        paymentReceivedNotification.receivePayment(dto);
//
//        ArgumentCaptor<Email> emailCaptor = ArgumentCaptor.forClass(Email.class);
//        verify(emailService, times(1)).send(emailCaptor.capture());
//        Email sentEmail = emailCaptor.getValue();
//
//        assertEquals("carlos@exemplo.com", sentEmail.getTo());
//        assertEquals("Recebemos seu pagamento!", sentEmail.getSubject());
//        assertTrue(sentEmail.getBody().contains("Carlos Eduardo"));
//        assertTrue(sentEmail.getBody().contains("R$ 1500.00"));
//        assertTrue(sentEmail.getBody().contains("parcela nº 5"));  // ajustado
//        assertTrue(sentEmail.getBody().contains(dto.loanId().toString()));
//
//        ArgumentCaptor<SMS> smsCaptor = ArgumentCaptor.forClass(SMS.class);
//        verify(smsService, times(1)).send(smsCaptor.capture());
//        SMS sentSms = smsCaptor.getValue();
//
//        assertEquals("+5511988776655", sentSms.getToNumber());
//        assertTrue(sentSms.getMessage().contains("Carlos Eduardo"));
//        assertTrue(sentSms.getMessage().contains("R$ 1500.00"));
//        assertTrue(sentSms.getMessage().contains("parcela nº 5"));  // ajustado
//        assertTrue(sentSms.getMessage().contains(dto.loanId().toString()));
//    }
//}

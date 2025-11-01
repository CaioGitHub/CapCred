//package com.capcredit.ms_notification.adapter.out;
//
//import com.capcredit.ms_notification.application.service.EmailService;
//import com.capcredit.ms_notification.application.service.SMSService;
//import com.capcredit.ms_notification.core.domain.Email;
//import com.capcredit.ms_notification.core.domain.SMS;
//import com.capcredit.ms_notification.port.out.dtos.ApprovedLoanDTO;
//import com.capcredit.ms_notification.port.out.dtos.UserDTO;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.ArgumentCaptor;
//
//import java.math.BigDecimal;
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.util.UUID;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//class ApprovedLoanNotificationImplTest {
//
//    private EmailService emailService;
//    private SMSService smsService;
//    private ApprovedLoanNotificationImpl approvedLoanNotification;
//
//    @BeforeEach
//    void setUp() {
//        emailService = mock(EmailService.class);
//        smsService = mock(SMSService.class);
//        approvedLoanNotification = new ApprovedLoanNotificationImpl(emailService, smsService);
//    }
//
//    @Test
//    void shouldSendEmailAndSMSWhenLoanIsApproved() {
//        UserDTO user = new UserDTO(
//                UUID.randomUUID(),
//                LocalDateTime.now(),
//                UUID.randomUUID(),
//                "João da Silva",
//                "joao@exemplo.com",
//                "+5511999999"
//        );
//        ApprovedLoanDTO dto = new ApprovedLoanDTO(
//                UUID.randomUUID(),
//                LocalDateTime.now(),
//                UUID.randomUUID(),
//                new UserDTO(
//                        UUID.randomUUID(),
//                        LocalDateTime.now(),
//                        UUID.randomUUID(),
//                        "João da Silva",
//                        "joao@exemplo.com",
//                        "+5511999999999"
//                ),
//                new BigDecimal("1050.00"),
//                12,
//                LocalDate.of(2025, 11, 10),
//                new BigDecimal("10000.00")
//        );
//
//        approvedLoanNotification.receiveApprovedLoan(dto);
//
//        ArgumentCaptor<Email> emailCaptor = ArgumentCaptor.forClass(Email.class);
//        verify(emailService, times(1)).send(emailCaptor.capture());
//        Email sentEmail = emailCaptor.getValue();
//
//        assertEquals("joao@exemplo.com", sentEmail.getTo());
//        assertEquals("Seu empréstimo foi aprovado!", sentEmail.getSubject());
//        assertTrue(sentEmail.getBody().contains("João da Silva"));
//        assertTrue(sentEmail.getBody().contains("R$ 1050.00"));
//
//        ArgumentCaptor<SMS> smsCaptor = ArgumentCaptor.forClass(SMS.class);
//        verify(smsService, times(1)).send(smsCaptor.capture());
//        SMS sentSms = smsCaptor.getValue();
//
//        assertEquals("+5511999999999", sentSms.getToNumber());
//        assertTrue(sentSms.getMessage().contains("João da Silva"));
//        assertTrue(sentSms.getMessage().contains("R$ 1050.00"));
//    }
//}

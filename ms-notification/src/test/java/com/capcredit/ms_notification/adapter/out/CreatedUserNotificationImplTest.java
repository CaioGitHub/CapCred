//package com.capcredit.ms_notification.adapter.out;
//
//import com.capcredit.ms_notification.application.service.EmailService;
//import com.capcredit.ms_notification.application.service.SMSService;
//import com.capcredit.ms_notification.core.domain.Email;
//import com.capcredit.ms_notification.core.domain.SMS;
//import com.capcredit.ms_notification.port.out.dtos.UserDTO;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.ArgumentCaptor;
//
//import java.time.LocalDateTime;
//import java.util.UUID;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//class CreatedUserNotificationImplTest {
//
//    private EmailService emailService;
//    private SMSService smsService;
//    private CreatedUserNotificationImpl createdUserNotification;
//
//    @BeforeEach
//    void setUp() {
//        emailService = mock(EmailService.class);
//        smsService = mock(SMSService.class);
//        createdUserNotification = new CreatedUserNotificationImpl(emailService, smsService);
//    }
//
//    @Test
//    void shouldSendWelcomeEmailAndSMSWhenUserIsCreated() {
//        UserDTO user = new UserDTO(
//                UUID.randomUUID(),
//                LocalDateTime.now(),
//                UUID.randomUUID(),
//                "Maria Oliveira",
//                "maria@exemplo.com",
//                "+5511988887777"
//        );
//
//        createdUserNotification.receiveCreatedUser(user);
//
//        ArgumentCaptor<Email> emailCaptor = ArgumentCaptor.forClass(Email.class);
//        verify(emailService, times(1)).send(emailCaptor.capture());
//        Email sentEmail = emailCaptor.getValue();
//
//        assertEquals("maria@exemplo.com", sentEmail.getTo());
//        assertEquals("Bem vindo ao CapCredit!", sentEmail.getSubject());
//        assertTrue(sentEmail.getBody().contains("Maria Oliveira"));
//        assertTrue(sentEmail.getBody().contains("bem vindo ao CapCredit"));
//
//        ArgumentCaptor<SMS> smsCaptor = ArgumentCaptor.forClass(SMS.class);
//        verify(smsService, times(1)).send(smsCaptor.capture());
//        SMS sentSms = smsCaptor.getValue();
//
//        assertEquals("+5511988887777", sentSms.getToNumber());
//        assertTrue(sentSms.getMessage().contains("Maria Oliveira"));
//        assertTrue(sentSms.getMessage().contains("bem vindo ao CapCredit"));
//    }
//}

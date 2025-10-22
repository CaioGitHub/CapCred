package com.capcredit.ms_notification.application.service;

import com.capcredit.ms_notification.application.exception.SMSSendException;
import com.capcredit.ms_notification.core.domain.SMS;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.rest.api.v2010.account.MessageCreator;
import com.twilio.type.PhoneNumber;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class SMSServiceTest {

    private SMSService smsService;

    @BeforeEach
    void setUp() {
        smsService = new SMSService();
        setPrivateField(smsService, "FROM_NUMBER", "+5511999999999");
    }

    private void setPrivateField(Object target, String fieldName, Object value) {
        try {
            var field = target.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(target, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void deveEnviarSMSComSucesso() {
        SMS sms = SMS.builder()
                .toNumber("+5511988887777")
                .message("Olá, seu empréstimo foi aprovado!")
                .build();

        try (MockedStatic<Message> mockedMessage = mockStatic(Message.class)) {
            MessageCreator mockCreator = mock(MessageCreator.class);
            Message mockMessage = mock(Message.class);

            mockedMessage.when(() ->
                    Message.creator(any(PhoneNumber.class), any(PhoneNumber.class), any(String.class))
            ).thenReturn(mockCreator);

            when(mockCreator.create()).thenReturn(mockMessage);

            assertDoesNotThrow(() -> smsService.send(sms));
        }
    }

    @Test
    void deveLancarExcecaoAoFalharEnvioSMS() {
        SMS sms = SMS.builder()
                .toNumber("+5511988887777")
                .message("Mensagem de teste")
                .build();

        try (MockedStatic<Message> mockedMessage = mockStatic(Message.class)) {
            MessageCreator mockCreator = mock(MessageCreator.class);

            mockedMessage.when(() ->
                    Message.creator(any(PhoneNumber.class), any(PhoneNumber.class), any(String.class))
            ).thenReturn(mockCreator);

            when(mockCreator.create()).thenThrow(new RuntimeException("Falha no Twilio"));

            assertThrows(SMSSendException.class, () -> smsService.send(sms));
        }
    }
}

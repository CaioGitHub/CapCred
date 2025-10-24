package com.capcredit.ms_notification.application.service;



import com.capcredit.ms_notification.application.exception.SMSSendException;
import com.capcredit.ms_notification.core.domain.SMS;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


@Slf4j
@Service
public class SMSService {

    @Value("${twilio.account-sid}")
    private String ACCOUNT_SID;

    @Value("${twilio.auth-token}")
    private String AUTH_TOKEN;

    @Value("${twilio.from-number}")
    private String FROM_NUMBER;

    @PostConstruct
    public void init() {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
    }

    public void send(SMS sms) {
        try {
            Message.creator(
                    new PhoneNumber(sms.getToNumber()),
                    new PhoneNumber(FROM_NUMBER),
                    sms.getMessage()
            ).create();
        }
     catch (RuntimeException e) {
        log.error("Erro ao enviar sms", e);
         throw new SMSSendException("Falha ao enviar SMS", e);
    }
    }
}

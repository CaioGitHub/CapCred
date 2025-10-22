package com.capcredit.ms_notification.application.service;



import com.capcredit.ms_notification.domain.model.SMS;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


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

    public void enviar(SMS sms) {
        Message.creator(
                new PhoneNumber(sms.getToNumber()),
                new PhoneNumber(FROM_NUMBER),
                sms.getMessage()
        ).create();
    }
}

package com.capcredit.ms_notification.adapter.out;

import com.capcredit.ms_notification.application.service.EmailService;
import com.capcredit.ms_notification.application.service.SMSService;
import com.capcredit.ms_notification.core.domain.Email;
import com.capcredit.ms_notification.core.domain.SMS;
import com.capcredit.ms_notification.port.out.dtos.UserDTO;
import com.capcredit.ms_notification.port.in.CreatedUserConsumerPortIn;
import org.springframework.stereotype.Component;

@Component
public class CreatedUserNotificationImpl implements CreatedUserConsumerPortIn {
    public static final String WELCOME = "Bem vindo ao CapCredit!";
    private final EmailService emailService;
    private final SMSService smsService;

    public CreatedUserNotificationImpl(EmailService emailService, SMSService smsService) {
        this.emailService = emailService;
        this.smsService = smsService;
    }

    @Override
    public void receiveCreatedUser(UserDTO dto) {
        notifyUserCreationByEmail(dto);
        notifyUserCreationBySMS(dto);
    }


    public void notifyUserCreationByEmail(UserDTO dto){
        Email email = Email.builder()
                .to(dto.email())
                .subject(WELCOME)
                .body(getBody(dto))
                .build();
        emailService.send(email);
    }

    public void notifyUserCreationBySMS(UserDTO dto){
        SMS sms = SMS.builder()
                        .toNumber(dto.phone())
                        .message(getBody(dto))
                         .build();
        smsService.send(sms);
    }

    private static String getBody(UserDTO dto) {
        return "Olá, " + dto.name() + ", bem vindo ao CapCredit. Seu cadastro foi realizado com sucesso.";
    }


}

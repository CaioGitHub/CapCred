package com.capcredit.ms_notification.adapter.out;

import com.capcredit.ms_notification.application.service.EmailService;
import com.capcredit.ms_notification.application.service.SMSService;
import com.capcredit.ms_notification.core.domain.Email;
import com.capcredit.ms_notification.core.domain.SMS;
import com.capcredit.ms_notification.port.in.DeniedLoanPortIn;
import com.capcredit.ms_notification.port.out.dtos.DeniedLoanDTO;
import org.springframework.stereotype.Component;


@Component
public class DeniedLoanNotificationImpl implements DeniedLoanPortIn {
    public static final String SUBJECT_DENIED = "Seu empréstimo foi negado!";
    private final EmailService emailService;
    private final SMSService smsService;

    public DeniedLoanNotificationImpl(EmailService emailService, SMSService smsService) {
        this.emailService = emailService;
        this.smsService = smsService;
    }

    @Override
    public void receiveDeniedLoan(DeniedLoanDTO dto) {
        notifyUserCreationByEmail(dto);
//        notifyUserCreationBySMS(dto);
    }


    public void notifyUserCreationByEmail(DeniedLoanDTO dto){
        Email email = Email.builder()
                .to(dto.user().email())
                .subject(SUBJECT_DENIED)
                .body(getBody(dto))
                .build();
        emailService.send(email);
    }

    public void notifyUserCreationBySMS(DeniedLoanDTO dto){
        SMS sms = SMS.builder()
                .toNumber(dto.user().phone())
                .message(getBody(dto))
                .build();
        smsService.send(sms);
    }

    private static String getBody(DeniedLoanDTO dto) {
        return "Olá, " + dto.user().name() + ", infelizmente seu empréstimo foi negado por motivo de " +
                dto.rejectionReason() + ". Não desanime, faremos uma nova análise de crédito em breve.";
    }


}

package com.capcredit.ms_notification.adapter.out;

import com.capcredit.ms_notification.application.service.EmailService;
import com.capcredit.ms_notification.application.service.SMSService;
import com.capcredit.ms_notification.core.domain.Email;
import com.capcredit.ms_notification.core.domain.SMS;
import com.capcredit.ms_notification.port.in.ApprovedLoanPortIn;
import com.capcredit.ms_notification.port.out.dtos.ApprovedLoanDTO;
import org.springframework.stereotype.Component;

@Component
public class ApprovedLoanNotificationImpl implements ApprovedLoanPortIn {
    public static final String SUBJECT_APPROVED = "Seu empréstimo foi aprovado!";
    private final EmailService emailService;
    private final SMSService smsService;

    public ApprovedLoanNotificationImpl(EmailService emailService, SMSService smsService) {
        this.emailService = emailService;
        this.smsService = smsService;
    }

    @Override
    public void receiveApprovedLoan(ApprovedLoanDTO dto) {
        notifyUserCreationByEmail(dto);
//        notifyUserCreationBySMS(dto);
    }


    public void notifyUserCreationByEmail(ApprovedLoanDTO dto){
        Email email = Email.builder()
                .to(dto.user().email())
                .subject(SUBJECT_APPROVED)
                .body(getBody(dto))
                .build();
        emailService.send(email);
    }

    public void notifyUserCreationBySMS(ApprovedLoanDTO dto){
        SMS sms = SMS.builder()
                .toNumber(dto.user().phone())
                .message(getBody(dto))
                .build();
        smsService.send(sms);
    }

    private static String getBody(ApprovedLoanDTO dto) {
        return "Olá, " + dto.user().name() + ", seu empréstimo no valor de " + dto.totalAmount() + " foi aprovado. Você deverá realizar " +
                dto.termInMonths() + " parcelas mensais de R$ " + dto.monthlyInstallmentValue() +
                ", com a primeira parcela vencendo em " + dto.firstDueDate() + ".";
    }


}

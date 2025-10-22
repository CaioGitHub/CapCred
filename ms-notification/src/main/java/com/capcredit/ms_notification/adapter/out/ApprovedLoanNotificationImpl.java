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
                .subject("Seu empréstimo foi aprovado!")
                .body("Olá, " + dto.user().name() + ", seu empréstimo foi aprovado. Você deverá realizar " +
                        dto.termInMonths() + " parcelas mensais de R$ " + dto.monthlyInstallmentValue() +
                        ", com a primeira parcela vencendo em " + dto.firstDueDate() + ".")
                .build();
        emailService.send(email);
    }

    public void notifyUserCreationBySMS(ApprovedLoanDTO dto){
        SMS sms = SMS.builder()
                .toNumber(dto.user().phone())
                .message("Olá, " + dto.user().name() + ", seu empréstimo foi aprovado. Você deverá realizar " +
                        dto.termInMonths() + " parcelas mensais de R$ " + dto.monthlyInstallmentValue() +
                        ", com a primeira parcela vencendo em " + dto.firstDueDate() + ".")
                .build();
        smsService.send(sms);
    }


}

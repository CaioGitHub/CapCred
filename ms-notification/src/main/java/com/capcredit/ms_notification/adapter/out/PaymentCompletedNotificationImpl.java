package com.capcredit.ms_notification.adapter.out;

import com.capcredit.ms_notification.application.service.EmailService;
import com.capcredit.ms_notification.application.service.SMSService;
import com.capcredit.ms_notification.core.domain.Email;
import com.capcredit.ms_notification.core.domain.SMS;
import com.capcredit.ms_notification.port.in.PaymentCompletedPortIn;
import com.capcredit.ms_notification.port.out.dtos.PaymentCompletedDTO;
import org.springframework.stereotype.Component;


@Component
public class PaymentCompletedNotificationImpl implements PaymentCompletedPortIn {
    public static final String SUBJECT_COMPLETED_PAYMENT = "Parabéns! Você quitou seu empréstimo!";
    private final EmailService emailService;
    private final SMSService smsService;

    public PaymentCompletedNotificationImpl(EmailService emailService, SMSService smsService) {
        this.emailService = emailService;
        this.smsService = smsService;
    }


    @Override
    public void receiveCompletedPayment(PaymentCompletedDTO dto) {
        notifyUserCreationByEmail(dto);
//        notifyUserCreationBySMS(dto);
    }


    public void notifyUserCreationByEmail(PaymentCompletedDTO dto){
        Email email = Email.builder()
                .to(dto.user().email())
                .subject(SUBJECT_COMPLETED_PAYMENT)
                .body(getBody(dto))
                .build();
        emailService.send(email);
    }

    public void notifyUserCreationBySMS(PaymentCompletedDTO dto){
        SMS sms = SMS.builder()
                .toNumber(dto.user().phone())
                .message(getBody(dto))
                .build();
        smsService.send(sms);
    }

    private static String getBody(PaymentCompletedDTO dto) {
        return "Olá, " + dto.user().name() + ".\n" +
                "\n" +
                "**🎉 PARABÉNS! SEU EMPRÉSTIMO ESTÁ QUITADO! 🎉**\n" +
                "\n" +
                "Temos o prazer de informar que o seu contrato de empréstimo (ID: " + dto.loanId() + ") \n" +
                "foi **TOTALMENTE LIQUIDADO** na data de " + dto.finalizationDate() + ".\n" +
                "\n" +
                "Sua dedicação em manter as parcelas em dia fortalece seu histórico de crédito.\n" +
                "\n" +
                "--------------------------------------------\n" +
                "Se precisar de um novo crédito no futuro, estaremos à disposição.\n" +
                "--------------------------------------------\n" +
                "\n" +
                "Atenciosamente,\n" +
                "Sua equipe CapCred - Soluções em Crédito\n" +
                "E-mail: capcred@capcred.com | Telefone: +55 (99) 9 9999-9999";
    }

}


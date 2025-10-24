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
        notifyUserCreationBySMS(dto);
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
        return "Olá, temos o prazer de informar que o seu empréstimo com ID " + dto.loanId() +
                " foi totalmente quitado em " + dto.finalizationDate() + ". " +
                "Agradecemos por confiar em nossos serviços e por manter suas parcelas em dia. " +
                "Se tiver alguma dúvida ou precisar de suporte, nossa equipe está à disposição.";
    }

}


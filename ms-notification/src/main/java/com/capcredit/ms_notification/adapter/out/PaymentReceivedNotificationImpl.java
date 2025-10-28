package com.capcredit.ms_notification.adapter.out;

import org.springframework.stereotype.Component;

import com.capcredit.ms_notification.application.service.EmailService;
import com.capcredit.ms_notification.application.service.SMSService;
import com.capcredit.ms_notification.core.domain.Email;
import com.capcredit.ms_notification.core.domain.SMS;
import com.capcredit.ms_notification.port.in.PaymentReceivedPortIn;
import com.capcredit.ms_notification.port.out.dtos.PaymentReceivedDTO;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PaymentReceivedNotificationImpl implements PaymentReceivedPortIn {

    public static final String SUBJECT_RECEIVED_PAYMENT = "Recebemos seu pagamento!";

    private final EmailService emailService;
    private final SMSService smsService;

    @Override
    public void receivePayment(PaymentReceivedDTO dto) {
        notifyUserCreationByEmail(dto);
        notifyUserCreationBySMS(dto);
    }

    private void notifyUserCreationByEmail(PaymentReceivedDTO dto) {
        Email email = Email.builder()
                .to(dto.user().email())
                .subject(SUBJECT_RECEIVED_PAYMENT)
                .body(getBody(dto))
                .build();

        emailService.send(email);
    }

    private void notifyUserCreationBySMS(PaymentReceivedDTO dto) {
        SMS sms = SMS.builder()
                .toNumber(dto.user().phone())
                .message(getBody(dto))
                .build();

        smsService.send(sms);
    }

    private String getBody(PaymentReceivedDTO dto) {
        return "Olá, " + dto.user().name() +
                ", recebemos o pagamento no valor de R$ " + dto.valuePaid() +
                " referente à parcela nº " + dto.installmentNumber() +
                " do empréstimo " + dto.loanId() + ". Obrigado por pagar em dia!";
    }
}

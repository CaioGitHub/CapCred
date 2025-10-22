package com.capcredit.ms_notification.adapter.out;

import com.capcredit.ms_notification.application.service.EmailService;
import com.capcredit.ms_notification.application.service.SMSService;
import com.capcredit.ms_notification.core.domain.Email;
import com.capcredit.ms_notification.core.domain.SMS;
import com.capcredit.ms_notification.port.in.PaymentReceivedPortIn;
import com.capcredit.ms_notification.port.out.dtos.DeniedLoanDTO;
import com.capcredit.ms_notification.port.out.dtos.PaymentReceivedDTO;
import org.springframework.stereotype.Component;


    @Component
    public class PaymentReceivedNotificationImpl implements PaymentReceivedPortIn {
        public static final String SUBJECT_RECEIVED_PAYMENT = "Recebemos seu pagamento!";
        private final EmailService emailService;
        private final SMSService smsService;

        public PaymentReceivedNotificationImpl(EmailService emailService, SMSService smsService) {
            this.emailService = emailService;
            this.smsService = smsService;
        }


        @Override
        public void receivePayment(PaymentReceivedDTO dto) {
            notifyUserCreationByEmail(dto);
//        notifyUserCreationBySMS(dto);
        }


        public void notifyUserCreationByEmail(PaymentReceivedDTO dto){
            Email email = Email.builder()
                    .to(dto.user().email())
                    .subject(SUBJECT_RECEIVED_PAYMENT)
                    .body(getBody(dto))
                    .build();
            emailService.send(email);
        }

        public void notifyUserCreationBySMS(PaymentReceivedDTO dto){
            SMS sms = SMS.builder()
                    .toNumber(dto.user().phone())
                    .message(getBody(dto))
                    .build();
            smsService.send(sms);
        }

        private static String getBody(PaymentReceivedDTO dto) {
            return "Olá, " + dto.user().name() + ", recebemos o pagamento de no valor de R$ " +
                    dto.valuePaid() + " referente à parcela de número " + dto.installmentNumber() + " do empréstimo de nº" + dto.loanId() + ". Obrigado por pagar em dia!";
        }

    }

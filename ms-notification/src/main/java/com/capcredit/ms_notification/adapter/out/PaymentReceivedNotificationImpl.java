package com.capcredit.ms_notification.adapter.out;

import com.capcredit.ms_notification.application.service.EmailService;
import com.capcredit.ms_notification.application.service.SMSService;
import com.capcredit.ms_notification.core.domain.Email;
import com.capcredit.ms_notification.core.domain.SMS;
import com.capcredit.ms_notification.core.domain.User;
import com.capcredit.ms_notification.port.out.UserRepository;
import com.capcredit.ms_notification.port.in.PaymentReceivedPortIn;
import com.capcredit.ms_notification.port.out.dtos.PaymentReceivedDTO;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
public class PaymentReceivedNotificationImpl implements PaymentReceivedPortIn {

    public static final String SUBJECT_RECEIVED_PAYMENT = "Recebemos seu pagamento!";

    private final EmailService emailService;
    private final SMSService smsService;
    private final UserRepository userRepository;

    private User user;

    public PaymentReceivedNotificationImpl(EmailService emailService,
                                           SMSService smsService,
                                           UserRepository userRepository) {
        this.emailService = emailService;
        this.smsService = smsService;
        this.userRepository = userRepository;
    }

    @Override
    public void receivePayment(PaymentReceivedDTO dto) {
        this.user = getUser(dto);

        notifyUserCreationByEmail(dto);
        notifyUserCreationBySMS(dto);
    }

    private void notifyUserCreationByEmail(PaymentReceivedDTO dto) {
        Email email = Email.builder()
                .to(user.getEmail())
                .subject(SUBJECT_RECEIVED_PAYMENT)
                .body(getBody(dto))
                .build();

        emailService.send(email);
    }

    private void notifyUserCreationBySMS(PaymentReceivedDTO dto) {
        SMS sms = SMS.builder()
                .toNumber(user.getPhone())
                .message(getBody(dto))
                .build();

        smsService.send(sms);
    }

    // TODO trazer número da parcela na mensagem
    private String getBody(PaymentReceivedDTO dto) {
        return "Olá, " + user.getName() +
                ", recebemos o pagamento no valor de R$ " + dto.valuePaid() +
                " referente à parcela nº " + dto.installmentNumber() +
                " do empréstimo " + dto.loanId() + ". Obrigado por pagar em dia!";
    }

    private User getUser(PaymentReceivedDTO dto) {
        return userRepository.findById(dto.userId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Usuário não encontrado para o ID: " + dto.userId()
                ));
    }
}

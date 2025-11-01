package com.capcredit.payment.port.out;

import com.capcredit.payment.port.out.dto.LoanCompletedDTO;
import com.capcredit.payment.port.out.dto.PaymentReceivedDTO;

public interface RabbitMqSender {
    void sendPaymentEvent(PaymentReceivedDTO paymentReceivedDTO);
    void sendLoanCompletedEvent(LoanCompletedDTO loanCompletedDTO);
}

package com.capcredit.payment.port.out;

import com.capcredit.payment.port.out.dto.InstallmentDTO;
import com.capcredit.payment.port.out.dto.LoanCompletedDTO;

public interface RabbitMqSender {
    void sendPaymentEvent(InstallmentDTO installmentDTO);
    void sendLoanCompletedEvent(LoanCompletedDTO loanCompletedDTO);
}

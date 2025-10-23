package com.capcredit.payment.port.out;

import com.capcredit.payment.port.out.dto.InstallmentDTO;

public interface RabbitMqSender {
    void sendPaymentEvent(InstallmentDTO installmentDTO);
}

package com.capcredit.ms_notification.port.in;

import com.capcredit.ms_notification.port.out.dtos.PaymentCompletedDTO;

public interface PaymentCompletedPortIn {
    void receiveCompletedPayment(PaymentCompletedDTO paymentCompletedDTO);
}


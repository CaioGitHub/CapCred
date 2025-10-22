package com.capcredit.ms_notification.port.in;

import com.capcredit.ms_notification.port.out.dtos.DeniedLoanDTO;
import com.capcredit.ms_notification.port.out.dtos.PaymentReceivedDTO;

public interface PaymentReceivedPortIn {
    void receivePayment(PaymentReceivedDTO paymentReceivedDTO);
}

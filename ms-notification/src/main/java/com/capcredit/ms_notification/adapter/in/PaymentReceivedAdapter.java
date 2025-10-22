package com.capcredit.ms_notification.adapter.in;

import com.capcredit.ms_notification.adapter.out.PaymentReceivedNotificationImpl;
import com.capcredit.ms_notification.port.in.PaymentReceivedPortIn;
import com.capcredit.ms_notification.port.out.dtos.PaymentReceivedDTO;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class PaymentReceivedAdapter implements PaymentReceivedPortIn {
    private final PaymentReceivedNotificationImpl paymentReceivedNotification;

    public PaymentReceivedAdapter(PaymentReceivedNotificationImpl paymentReceivedNotification) {
        this.paymentReceivedNotification = paymentReceivedNotification;
    }


    @RabbitListener(queues = "${broker.queue.received.payment}")
    public void receivePayment(PaymentReceivedDTO dto) {
        paymentReceivedNotification.receivePayment(dto);
    }
}

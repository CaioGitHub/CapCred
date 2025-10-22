package com.capcredit.ms_notification.adapter.in;

import com.capcredit.ms_notification.adapter.out.PaymentCompletedNotificationImpl;
import com.capcredit.ms_notification.port.in.PaymentCompletedPortIn;
import com.capcredit.ms_notification.port.out.dtos.PaymentCompletedDTO;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class PaymentCompletedAdapter implements PaymentCompletedPortIn {
    private final PaymentCompletedNotificationImpl paymentCompletedNotification;

    public PaymentCompletedAdapter(PaymentCompletedNotificationImpl paymentCompletedNotification) {
        this.paymentCompletedNotification = paymentCompletedNotification;
    }


    @RabbitListener(queues = "${broker.queue.completed.payment}")
    public void receiveCompletedPayment(PaymentCompletedDTO dto) {
        paymentCompletedNotification.receiveCompletedPayment(dto);
    }
}


package com.capcredit.ms_notification.adapter.in;

import com.capcredit.ms_notification.adapter.out.PaymentCompletedNotificationImpl;
import com.capcredit.ms_notification.port.out.dtos.PaymentCompletedDTO;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PaymentCompletedAdapter  {

    private final PaymentCompletedNotificationImpl paymentCompletedNotification;

    @Value("${broker.queue.completed.loan.notification}")
    private String completedLoanNotificationQueue;

    public PaymentCompletedAdapter(PaymentCompletedNotificationImpl paymentCompletedNotification) {
        this.paymentCompletedNotification = paymentCompletedNotification;
    }

    @RabbitListener(queues = "${broker.queue.completed.loan.notification}")
    public void receiveCompletedPayment(PaymentCompletedDTO dto) {
        paymentCompletedNotification.receiveCompletedPayment(dto);
    }
}


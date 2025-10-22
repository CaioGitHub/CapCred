package com.capcredit.ms_notification.adapter.in;

import com.capcredit.ms_notification.adapter.out.DeniedLoanNotificationImpl;
import com.capcredit.ms_notification.port.in.DeniedLoanPortIn;
import com.capcredit.ms_notification.port.out.dtos.DeniedLoanDTO;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class DeniedLoanConsumerAdapter implements DeniedLoanPortIn {
    private final DeniedLoanNotificationImpl deniedLoanNotification;

    public DeniedLoanConsumerAdapter(DeniedLoanNotificationImpl deniedLoanNotification) {
        this.deniedLoanNotification = deniedLoanNotification;
    }

    @RabbitListener(queues = "${broker.queue.denied.loan}")
    public void receiveDeniedLoan(DeniedLoanDTO dto) {
        deniedLoanNotification.receiveDeniedLoan(dto);
    }

}

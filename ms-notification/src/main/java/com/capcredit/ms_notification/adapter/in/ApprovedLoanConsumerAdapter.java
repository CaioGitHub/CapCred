package com.capcredit.ms_notification.adapter.in;

import com.capcredit.ms_notification.adapter.out.ApprovedLoanNotificationImpl;
import com.capcredit.ms_notification.port.in.ApprovedLoanPortIn;
import com.capcredit.ms_notification.port.out.dtos.ApprovedLoanDTO;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class ApprovedLoanConsumerAdapter implements ApprovedLoanPortIn {
    private final ApprovedLoanNotificationImpl approvedLoanNotification;
    public ApprovedLoanConsumerAdapter(ApprovedLoanNotificationImpl approvedLoanNotification) {
        this.approvedLoanNotification = approvedLoanNotification;
    }
    @RabbitListener(queues = "${broker.queue.approved.loan}")
    public void receiveApprovedLoan(ApprovedLoanDTO dto) {
        approvedLoanNotification.receiveApprovedLoan(dto);
    }

}

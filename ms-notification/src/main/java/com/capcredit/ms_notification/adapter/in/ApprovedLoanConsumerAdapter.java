package com.capcredit.ms_notification.adapter.in;

import com.capcredit.ms_notification.adapter.out.ApprovedLoanNotificationImpl;
import com.capcredit.ms_notification.port.in.ApprovedLoanPortIn;
import com.capcredit.ms_notification.port.out.dtos.ApprovedLoanDTO;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ApprovedLoanConsumerAdapter implements ApprovedLoanPortIn {

    private final ApprovedLoanNotificationImpl approvedLoanNotification;

    @Value("${broker.queue.approved.loan.notification}")
    private String approvedLoanNotificationQueue;

    public ApprovedLoanConsumerAdapter(ApprovedLoanNotificationImpl approvedLoanNotification) {
        this.approvedLoanNotification = approvedLoanNotification;
    }

    @RabbitListener(queues = {"${broker.queue.approved.loan.notification}"})
    public void receiveApprovedLoan(ApprovedLoanDTO dto) {
        try {
            approvedLoanNotification.receiveApprovedLoan(dto);
        } catch (Exception e) {
            System.err.println("Erro ao processar notificação de empréstimo aprovado: " + dto.loanId());
            e.printStackTrace();
        }
    }
}

package com.capcredit.payment.adapters.in.messaging.consumers;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.capcredit.payment.core.service.PaymentService;
import com.capcredit.payment.port.in.LoanPortIn;
import com.capcredit.payment.port.in.dtos.LoanApprovedDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class LoanConsumer implements LoanPortIn {

    private final PaymentService paymentService;

//    @Override
//    @RabbitListener(queues = "${broker.queue.approved.loan}")
//    public void processLoanApproved(LoanApprovedDTO dto) {
//        log.info("Event({}) - Received approved loan event for loan {}", dto.getEventId(), dto.getLoanId());
//        paymentService.createInstallments(dto.toDomain());
//    }

    @Value("${broker.queue.approved.loan.payment}")
    private String approvedLoanPaymentQueue;


    @Override
    @RabbitListener(queues = {"${broker.queue.approved.loan.payment}"}) // 🚨 ALTERAÇÃO CRÍTICA
    public void processLoanApproved(LoanApprovedDTO dto) {
        log.info("Event({}) - Received approved loan event for loan {}", dto.getEventId(), dto.getLoanId());

        try {
            paymentService.createInstallments(dto.toDomain());
        } catch (Exception e) {
            log.error("Error creating installments for loan {}", dto.getLoanId(), e);
        }
    }
}

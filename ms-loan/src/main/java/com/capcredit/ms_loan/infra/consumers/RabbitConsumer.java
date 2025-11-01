package com.capcredit.ms_loan.infra.consumers;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.capcredit.ms_loan.application.ports.in.EventConsumer;
import com.capcredit.ms_loan.application.services.LoanService;
import com.capcredit.ms_loan.domain.events.LoanCompleted;
import com.capcredit.ms_loan.domain.events.LoanCreated;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class RabbitConsumer implements EventConsumer {

    private final LoanService service;

    @Value("${broker.queue.loan.completed.loan}")
    private String loanCompletedLoanQueue;

    @Override
    @RabbitListener(queues = "${broker.queue.loan.created}")
    public void processCreatedLoan(LoanCreated event) {
        log.info("Event({}) - Received loan created event for loan {}", event.getEventId(), event.getLoanId());
        service.processLoan(event.getLoanId());
    }

    @Override
    @RabbitListener(queues = {"${broker.queue.loan.completed.loan}"})
    public void processCompletedLoan(LoanCompleted event) {
        try {
            log.info("Event({}) - Received loan completed event for loan {}", event.getEventId(), event.getLoanId());
            service.closeLoan(event.getLoanId());
        } catch (Exception e) {
            log.error("Error processing completed loan event for loan {}", event.getLoanId(), e);
        }
    }
    
}

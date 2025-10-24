package com.capcredit.ms_loan.infra.consumers;

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

    @Override
    public void processCreatedLoan(LoanCreated event) {
        log.info("Event({}) - Received loan created event for loan {}", event.getEventId(), event.getLoanId());
        service.processLoan(event.getLoanId());
    }

    @Override
    public void processCompletedLoan(LoanCompleted event) {
        log.info("Event({}) - Received loan completed event for loan {}", event.getEventId(), event.getLoanId());
        service.closeLoan(event.getLoanId());
    }
    
}

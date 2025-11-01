package com.capcredit.ms_loan.infra.adapters;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.capcredit.ms_loan.application.ports.out.EventPublisherPort;
import com.capcredit.ms_loan.domain.events.LoanApproved;
import com.capcredit.ms_loan.domain.events.LoanCreated;
import com.capcredit.ms_loan.domain.events.LoanRejected;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class RabbitPublisher implements EventPublisherPort {

    @Value("${broker.queue.loan.created}")
    public String loanCreatedQueue;

    @Value("${broker.exchange.loan}")
    public String loanExchange;

    @Value("${broker.queue.loan.rejected}")
    public String loanRejectedQueue;

    private final RabbitTemplate template;

    @Override
    public void publishLoanCreated(LoanCreated loanCreated) {
        log.info("Event({}) - Publishing loan created event for loan {}", loanCreated.getEventId(), loanCreated.getLoanId());
        template.convertAndSend(loanCreatedQueue, loanCreated);
    }

//    @Override
//    public void publishLoanApproved(LoanApproved loanApproved) {
//        log.info("Event({}) - Publishing loan approved event for loan {}", loanApproved.getEventId(), loanApproved.getLoanId());
//        template.convertAndSend(loanApprovedQueue, loanApproved);
//    }

    @Override
    public void publishLoanApproved(LoanApproved loanApproved) {
        log.info("Event({}) - Publishing loan approved event for loan {}", loanApproved.getEventId(), loanApproved.getLoanId());

        template.convertAndSend(
                loanExchange,
                "loan.approved",
                loanApproved
        );
    }

    @Override
    public void publishLoanRejected(LoanRejected loanRejected) {
        log.info("Event({}) - Publishing loan rejected event for loan {}", loanRejected.getEventId(), loanRejected.getLoanId());
        template.convertAndSend(loanRejectedQueue, loanRejected);
    }
    
}

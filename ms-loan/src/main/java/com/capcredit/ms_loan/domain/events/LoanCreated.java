package com.capcredit.ms_loan.domain.events;

import java.time.LocalDateTime;
import java.util.UUID;

import com.capcredit.ms_loan.domain.model.Loan;

public class LoanCreated {
    private UUID eventId;
    private UUID loanId;
    private LocalDateTime timestamp;

    public LoanCreated() { }

    public LoanCreated(Loan loan) {
        this.eventId = UUID.randomUUID();
        this.loanId = loan.getId();
        this.timestamp = LocalDateTime.now();
    }

    public UUID getEventId() {
        return eventId;
    }

    public void setEventId(UUID eventId) {
        this.eventId = eventId;
    }

    public UUID getLoanId() {
        return loanId;
    }

    public void setLoanId(UUID loanId) {
        this.loanId = loanId;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}

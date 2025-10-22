package com.capcredit.ms_loan.domain.events;

import java.time.LocalDateTime;
import java.util.UUID;

public class LoanCompleted {
    private UUID eventId;
    private UUID loanId;
    private LocalDateTime timestamp;

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

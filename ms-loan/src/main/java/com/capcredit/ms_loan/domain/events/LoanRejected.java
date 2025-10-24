package com.capcredit.ms_loan.domain.events;

import java.time.LocalDateTime;
import java.util.UUID;

import com.capcredit.ms_loan.domain.model.User;

public class LoanRejected {
    private UUID eventId;
    private User user;
    private UUID loanId;
    private String rejectionReason;
    private LocalDateTime timestamp;

    public LoanRejected(User user, UUID loanId, String rejectionReason) {
        this.eventId = UUID.randomUUID();
        this.user = user;
        this.loanId = loanId;
        this.rejectionReason = rejectionReason;
        this.timestamp = LocalDateTime.now();
    }

    public UUID getEventId() {
        return eventId;
    }

    public User getUser() {
        return user;
    }

    public UUID getLoanId() {
        return loanId;
    }

    public String getRejectionReason() {
        return rejectionReason;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}
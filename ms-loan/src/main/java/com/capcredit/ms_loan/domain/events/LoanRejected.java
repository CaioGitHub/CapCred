package com.capcredit.ms_loan.domain.events;

import java.time.LocalDateTime;
import java.util.UUID;

import com.capcredit.ms_loan.domain.model.User;

public class LoanRejected {
    private UUID eventId;
    private UserDTO user;
    private UUID loanId;
    private String rejectionReason;
    private LocalDateTime timestamp;

    public LoanRejected(User user, UUID loanId, String rejectionReason) {
        this.eventId = UUID.randomUUID();
        this.user = new UserDTO(user);
        this.loanId = loanId;
        this.rejectionReason = rejectionReason;
        this.timestamp = LocalDateTime.now();
    }

    public UUID getEventId() {
        return eventId;
    }

    public UserDTO getUser() {
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

    static class UserDTO {
        private UUID userId;
        private String name;
        private String email;

        public UserDTO(User user) {
            this.userId = user.getId();
            this.name = user.getName();
            this.email = user.getEmail();
        }

        public UUID getUserId() {
            return userId;
        }

        public String getName() {
            return name;
        }

        public String getEmail() {
            return email;
        }
    }
}
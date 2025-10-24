package com.capcredit.ms_loan.domain.events;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import com.capcredit.ms_loan.domain.model.Loan;
import com.capcredit.ms_loan.domain.model.User;

public class LoanApproved {
    private UUID eventId;
    private UserDTO user;
    private UUID loanId;
    private Integer termInMonths;
    private BigDecimal totalAmount;
    private BigDecimal monthlyInstallmentValue;
    private LocalDate firstDueDate;
    private LocalDateTime timestamp;

    public LoanApproved(User user, Loan loan) {
        this.eventId = UUID.randomUUID();
        this.user = new UserDTO(user);
        this.loanId = loan.getId();
        this.termInMonths = loan.getTermInMonths();
        this.totalAmount = loan.getRequestedAmount();
        this.monthlyInstallmentValue = loan.getMonthlyInstallmentValue();
        this.firstDueDate = loan.getFirstDueDate();
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

    public Integer getTermInMonths() {
        return termInMonths;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public BigDecimal getMonthlyInstallmentValue() {
        return monthlyInstallmentValue;
    }

    public LocalDate getFirstDueDate() {
        return firstDueDate;
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

package com.capcredit.ms_loan.infra.entities;

import static jakarta.persistence.EnumType.STRING;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import com.capcredit.ms_loan.domain.enums.LoanStatus;
import com.capcredit.ms_loan.domain.enums.RequestStatus;
import com.capcredit.ms_loan.domain.model.Loan;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "loan")
@NoArgsConstructor
@AllArgsConstructor
public class LoanEntity {

    @Id
    private UUID id;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(name = "requested_amount", nullable = false)
    private BigDecimal requestedAmount;

    @Column(name = "term_in_months", nullable = false)
    private Integer termInMonths;

    @Column(name = "applied_rate", nullable = false)
    private BigDecimal appliedRate;

    @Enumerated(STRING)
    @Column(name = "request_status", nullable = false)
    private RequestStatus requestStatus;

    @Enumerated(STRING)
    @Column(name = "loan_status", nullable = false)
    private LoanStatus loanStatus;

    @Column(name = "first_due_date", nullable = false)
    private LocalDate firstDueDate;

    public static LoanEntity from(Loan loan) {
        return new LoanEntity(
            loan.getId(),
            loan.getUserId(),
            loan.getRequestedAmount(),
            loan.getTermInMonths(),
            loan.getAppliedRate(),
            loan.getRequestStatus(),
            loan.getLoanStatus(),
            loan.getFirstDueDate()
        );
    }

    public Loan toDomain() {
        return new Loan(
            id,
            userId,
            requestedAmount,
            termInMonths,
            appliedRate,
            firstDueDate,
            requestStatus,
            loanStatus
        );
    }
}

package com.capcredit.ms_loan.interfaces.dtos;

import static com.capcredit.ms_loan.domain.enums.LoanStatus.ACTIVE;
import static com.capcredit.ms_loan.domain.enums.RequestStatus.PENDING;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import com.capcredit.ms_loan.domain.model.Loan;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestLoanDTO {
    private UUID userId;
    private BigDecimal requestedAmount;
    private Integer termInMonths;
    private LocalDate firstDueDate;

    public Loan toDomain(BigDecimal rate) {
        return new Loan(userId, requestedAmount, termInMonths, rate, firstDueDate, PENDING, ACTIVE);
    }
}

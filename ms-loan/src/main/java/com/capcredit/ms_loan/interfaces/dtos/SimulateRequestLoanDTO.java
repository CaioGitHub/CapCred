package com.capcredit.ms_loan.interfaces.dtos;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.capcredit.ms_loan.domain.model.Loan;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SimulateRequestLoanDTO {
    
    @NotNull(message = "Requested amount cannot be null")
    @Positive(message = "Requested amount must be positive")
    private BigDecimal requestedAmount;

    @NotNull(message = "Term in months cannot be null")
    @Positive(message = "Term in months must be positive")
    private Integer termInMonths;

    @NotNull(message = "First due date cannot be null")
    @FutureOrPresent(message = "First due date must be today or in the future")
    private LocalDate firstDueDate;

    public Loan toDomain(BigDecimal rate) {
        return new Loan(null, requestedAmount, termInMonths, rate, firstDueDate, null, null);
    }
}

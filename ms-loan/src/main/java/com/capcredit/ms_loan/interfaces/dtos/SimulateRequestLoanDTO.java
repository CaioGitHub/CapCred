package com.capcredit.ms_loan.interfaces.dtos;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.capcredit.ms_loan.domain.model.Loan;

import lombok.AllArgsConstructor;
@AllArgsConstructor
public class SimulateRequestLoanDTO {
    
    private BigDecimal requestedAmount;
    private Integer termInMonths;
    private LocalDate firstDueDate;

    public Loan toDomain(BigDecimal rate) {
        return new Loan(null, requestedAmount, termInMonths, rate, firstDueDate, null, null);
    }
}

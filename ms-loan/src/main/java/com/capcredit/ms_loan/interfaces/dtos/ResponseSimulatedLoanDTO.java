package com.capcredit.ms_loan.interfaces.dtos;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.capcredit.ms_loan.domain.model.Loan;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseSimulatedLoanDTO {
    private BigDecimal requestedAmount;
    private Integer termInMonths;
    private BigDecimal appliedRate;
    private BigDecimal monthlyInstallmentValue;
    private LocalDate firstDueDate;

    public static ResponseSimulatedLoanDTO from(Loan loan) {
        return new ResponseSimulatedLoanDTO(
            loan.getRequestedAmount(),
            loan.getTermInMonths(),
            loan.getAppliedRate(),
            loan.getMonthlyInstallmentValue(),
            loan.getFirstDueDate()
        );
    }
}

package com.capcredit.ms_loan.interfaces.dtos;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.capcredit.ms_loan.domain.enums.LoanStatus;
import com.capcredit.ms_loan.domain.enums.RequestStatus;
import com.capcredit.ms_loan.domain.model.Loan;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseLoanDTO {
    private BigDecimal requestedAmount;
    private Integer termInMonths;
    private BigDecimal appliedRate;
    private BigDecimal monthlyInstallmentValue;
    private RequestStatus requestStatus;
    private LoanStatus loanStatus;
    private LocalDate firstDueDate;

    public static ResponseLoanDTO from(Loan loan) {
        return new ResponseLoanDTO(
            loan.getRequestedAmount(),
            loan.getTermInMonths(),
            loan.getAppliedRate(),
            loan.getMonthlyInstallmentValue(),
            loan.getRequestStatus(),
            loan.getLoanStatus(),
            loan.getFirstDueDate()
        );
    }
}

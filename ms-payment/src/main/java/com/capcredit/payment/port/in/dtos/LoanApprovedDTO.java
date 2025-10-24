package com.capcredit.payment.port.in.dtos;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import com.capcredit.payment.core.domain.model.Loan;
import com.capcredit.payment.port.out.dto.UserDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoanApprovedDTO {
    
    private UUID eventId;
    private UserDTO user;
    private UUID loanId;
    private Integer termInMonths;
    private BigDecimal totalAmount;
    private BigDecimal monthlyInstallmentValue;
    private LocalDate firstDueDate;
    private LocalDateTime timestamp;

    public Loan toDomain() {
        return Loan.builder()
            .id(this.loanId)
            .termInMonths(this.termInMonths)
            .monthlyInstallmentValue(this.monthlyInstallmentValue)
            .firstDueDate(this.firstDueDate)
            .userId(this.user.userId())
            .build();
    }
}

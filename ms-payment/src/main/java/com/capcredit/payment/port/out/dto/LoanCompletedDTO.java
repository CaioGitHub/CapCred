package com.capcredit.payment.port.out.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import com.capcredit.payment.core.domain.model.Installment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoanCompletedDTO {
    private UUID eventId;
    private LocalDateTime timestamp;
    private UUID loanId;
    private LocalDate finalizationDate;
    private UserDTO user;

    public static LoanCompletedDTO fromDomain(Installment installment, UserDTO user) {
        return new LoanCompletedDTO(
            UUID.randomUUID(),
            LocalDateTime.now(),
            installment.getLoanId(),
            installment.getPaymentDate().toLocalDate(),
            user
        );
    }
}

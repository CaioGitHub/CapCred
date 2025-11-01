package com.capcredit.payment.port.out.dto;

import java.math.BigDecimal;
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
public class PaymentReceivedDTO {
    private UUID installmentId;
    private UUID loanId;
    private Integer installmentNumber;
    private BigDecimal valueDue;
    private LocalDate dueDate;
    private LocalDateTime paymentDate;
    private BigDecimal valuePaid;
    private String paymentStatus;
    private UserDTO user;

    public static PaymentReceivedDTO fromDomain(Installment installment, UserDTO user) {
        return new PaymentReceivedDTO(
            installment.getId(),
            installment.getLoanId(),
            installment.getInstallmentNumber(),
            installment.getValueDue(),
            installment.getDueDate(),
            installment.getPaymentDate(),
            installment.getValuePaid(),
            installment.getPaymentStatus().name(),
            user
        );
    }
}

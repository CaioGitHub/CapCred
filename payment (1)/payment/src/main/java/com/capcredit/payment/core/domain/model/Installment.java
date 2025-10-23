package com.capcredit.payment.core.domain.model;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Installment {

    @Id
    private UUID id;
    private UUID loanId;
    private BigDecimal valueDue;
    private LocalDate dueDate;
    private LocalDateTime paymentDate;
    private BigDecimal valuePaid;
    private PaymentStatus paymentStatus;



}

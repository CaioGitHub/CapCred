package com.capcredit.payment.core.domain.model;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Loan {
    private UUID id;
    private Integer termInMonths;
    private BigDecimal monthlyInstallmentValue;
    private LocalDate firstDueDate;
    private UUID userId;
}


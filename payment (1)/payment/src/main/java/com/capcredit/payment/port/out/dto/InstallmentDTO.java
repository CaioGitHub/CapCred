package com.capcredit.payment.port.out.dto;


import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record InstallmentDTO(
        UUID installmentId,
        UUID loanId,
        BigDecimal valueDue,
        LocalDate dueDate,
        LocalDateTime paymentDate,
        BigDecimal valuePaid,
        String paymentStatus,
        UUID userId
) {}

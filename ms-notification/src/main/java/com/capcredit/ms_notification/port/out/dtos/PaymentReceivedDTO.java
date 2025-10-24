package com.capcredit.ms_notification.port.out.dtos;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record PaymentReceivedDTO(
        UUID installmentId,
        UUID loanId,
        Integer installmentNumber,
        BigDecimal valueDue,
        LocalDate dueDate,
        LocalDateTime paymentDate,
        BigDecimal valuePaid,
        String paymentStatus,
        UUID userId
) {}

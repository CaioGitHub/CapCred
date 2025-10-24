package com.capcredit.payment.port.out.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record PaymentDTO(
        UUID id,
        UUID installmentId,
        LocalDateTime paymentDate,
        BigDecimal amountPaid
) {}

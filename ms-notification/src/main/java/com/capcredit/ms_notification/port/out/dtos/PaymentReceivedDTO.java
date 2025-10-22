package com.capcredit.ms_notification.port.out.dtos;

import org.apache.catalina.User;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record PaymentReceivedDTO(
        UUID eventId,
        LocalDateTime timestamp,
        UUID installmentId,
        UUID loanId,
        Integer installmentNumber,
        BigDecimal valuePaid,
        LocalDate paymentDate,
        UserDTO user
) {}
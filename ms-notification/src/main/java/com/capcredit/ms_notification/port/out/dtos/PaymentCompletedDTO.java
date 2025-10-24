package com.capcredit.ms_notification.port.out.dtos;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record PaymentCompletedDTO(
        UUID eventId,
        LocalDateTime timestamp,
        UUID loanId,
        UserDTO user,
        LocalDate finalizationDate
) {}

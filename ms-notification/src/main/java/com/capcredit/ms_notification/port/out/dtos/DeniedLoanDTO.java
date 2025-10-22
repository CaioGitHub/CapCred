package com.capcredit.ms_notification.port.out.dtos;

import java.time.LocalDateTime;
import java.util.UUID;

public record DeniedLoanDTO(
        UUID eventId,
        LocalDateTime timestamp,
        UUID loanId,
        UUID userId,
        String rejectionReason
){}

package com.capcredit.payment.port.out.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record UserDTO(
        UUID eventId,
        LocalDateTime timestamp,
        UUID userId,
        String name,
        String email,
        String phone
) {}

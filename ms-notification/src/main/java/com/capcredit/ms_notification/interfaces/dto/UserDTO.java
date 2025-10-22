package com.capcredit.ms_notification.interfaces.dto;

import java.util.UUID;

public record UserDTO(UUID userId, String name, String email, String phone) {
}

package com.capcredit.payment.port.out.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import com.capcredit.payment.core.domain.model.User;

public record UserDTO(
	UUID eventId,
	LocalDateTime timestamp,
	UUID userId,
	String name,
	String email,
	String phone
) {
	public static UserDTO fromDomain(User user) {
		return new UserDTO(
			UUID.randomUUID(),
			LocalDateTime.now(),
			user.getId(),
			user.getName(),
			user.getEmail(),
			user.getPhone());
	}
}

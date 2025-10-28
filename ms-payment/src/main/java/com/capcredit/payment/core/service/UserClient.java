package com.capcredit.payment.core.service;

import java.util.Optional;
import java.util.UUID;

import com.capcredit.payment.core.domain.model.User;

public interface UserClient {
    Optional<User> findById(UUID userId);
}

package com.capcredit.ms_loan.application.ports.out;

import java.util.Optional;
import java.util.UUID;

import com.capcredit.ms_loan.domain.model.User;

public interface UserClientPort {
    Optional<User> findById(UUID userId);
}

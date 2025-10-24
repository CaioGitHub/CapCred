package com.capcredit.ms_loan.infra.adapters;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.capcredit.ms_loan.application.ports.out.UserClientPort;
import com.capcredit.ms_loan.domain.model.User;
import com.capcredit.ms_loan.infra.clients.UserFeignClient;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserClientAdapter implements UserClientPort {

    private final UserFeignClient userClient;

    @Override
    public Optional<User> findById(UUID id) {
        try {
            return Optional.ofNullable(userClient.findById(id));
        } catch(Exception ex) {
            log.error("Error finding user by id: {}", id, ex);
            return Optional.empty();
        }
    }
}

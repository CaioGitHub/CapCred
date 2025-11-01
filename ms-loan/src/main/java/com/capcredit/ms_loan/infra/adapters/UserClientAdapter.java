package com.capcredit.ms_loan.infra.adapters;

import java.util.Optional;
import java.util.UUID;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
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
    @CircuitBreaker(name = "user-service-call", fallbackMethod = "fallbackFindById")
    public Optional<User> findById(UUID id) {
        try {
            return Optional.ofNullable(userClient.findById(id));
        } catch(Exception ex) {
            log.error("Error finding user by id: {}", id, ex);
            return Optional.empty();
        }
    }

    public Optional<User> fallbackFindById(UUID id, Throwable t) {
        log.error("Circuit Breaker OPEN or failed for user-service-call. ID: {}. Causa: {}", id, t.getMessage());
        return Optional.empty();
    }
}

package com.capcredit.payment.adapters.out.clients;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.capcredit.payment.adapters.out.feign.UserFeignClient;
import com.capcredit.payment.core.domain.model.User;
import com.capcredit.payment.core.service.UserClient;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserClientAdapter implements UserClient {

    private final UserFeignClient userFeignClient;

    @Override
    public Optional<User> findById(UUID id) {
        try {
            return Optional.ofNullable(userFeignClient.findById(id));
        } catch (Exception e) {
            log.error("Error fetching user with id {}", id, e);
            return Optional.empty();
        }
    }
}

package com.capcredit.payment.config.security;

import static com.capcredit.payment.core.domain.enums.UserRole.ADMIN;

import java.util.UUID;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.capcredit.payment.core.service.PaymentService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class AccessValidator {

    private final PaymentService paymentService;

    public boolean canAccess(UUID installmentId, Authentication authentication) {
        try {
            var userId = UUID.fromString(authentication.getName());
            var installment = paymentService.findById(installmentId);
            return installment.getUserId().equals(userId) || isAdmin(authentication);
        } catch (Exception e) {
            return false;
        }
    }

    private boolean isAdmin(Authentication authentication) {
        return authentication.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals(ADMIN.name()));
    }
}
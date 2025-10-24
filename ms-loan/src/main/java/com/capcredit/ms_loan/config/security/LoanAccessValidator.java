package com.capcredit.ms_loan.config.security;

import static com.capcredit.ms_loan.domain.enums.UserRole.ADMIN;

import java.util.UUID;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.capcredit.ms_loan.application.services.LoanService;
import com.capcredit.ms_loan.domain.model.LoanFilter;
import com.capcredit.ms_loan.interfaces.dtos.RequestLoanDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class LoanAccessValidator {

    private final LoanService loanService;

    public boolean canAccessLoan(UUID loanId, Authentication authentication) {
        try {
            var userId = UUID.fromString(authentication.getName());
            var loan = loanService.findById(loanId);
            return loan.getUserId().equals(userId) || authentication.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals(ADMIN.name()));
        } catch (Exception e) {
            return false;
        }
    }

    public boolean canAccessLoan(LoanFilter filter, Authentication authentication) {
        var userId = UUID.fromString(authentication.getName());
        if(authentication.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals(ADMIN.name()))) {
            return true;
        } else if(filter.getUserId() == null) {
            throw new AccessDeniedException("User ID must be provided");
        }
        return filter.getUserId().equals(userId);
    }

    public boolean canAccessLoan(RequestLoanDTO dto, Authentication authentication) {
        try {
            var userId = UUID.fromString(authentication.getName());
            return dto.getUserId().equals(userId) || authentication.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals(ADMIN.name()));
        } catch (Exception e) {
            return false;
        }
    }
}
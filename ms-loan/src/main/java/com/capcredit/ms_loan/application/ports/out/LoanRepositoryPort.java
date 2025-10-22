package com.capcredit.ms_loan.application.ports.out;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.capcredit.ms_loan.domain.model.Loan;
import com.capcredit.ms_loan.domain.model.LoanFilter;

public interface LoanRepositoryPort {
    void save(Loan loan);
    Page<Loan> findAll(LoanFilter filter, Pageable pageable);
    Optional<Loan> findById(UUID id);
}

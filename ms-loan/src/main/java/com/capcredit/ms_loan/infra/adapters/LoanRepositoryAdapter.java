package com.capcredit.ms_loan.infra.adapters;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import com.capcredit.ms_loan.application.ports.out.LoanRepositoryPort;
import com.capcredit.ms_loan.domain.model.Loan;
import com.capcredit.ms_loan.domain.model.LoanFilter;
import com.capcredit.ms_loan.infra.entities.LoanEntity;
import com.capcredit.ms_loan.infra.repositories.LoanJpaRepository;
import com.capcredit.ms_loan.infra.specifications.LoanSpecifications;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class LoanRepositoryAdapter implements LoanRepositoryPort {

    private final LoanJpaRepository loanRepository;

    @Override
    public void save(Loan loan) {
        loanRepository.save(LoanEntity.from(loan));
    }

    @Override
    public Page<Loan> findAll(LoanFilter filter, Pageable pageable) {
        Specification<LoanEntity> specification = LoanSpecifications.build()
            .and(LoanSpecifications.withUserId(filter.getUserId()))
            .and(LoanSpecifications.withLoanStatus(filter.getLoanStatus()))
            .and(LoanSpecifications.withRequestStatus(filter.getRequestStatus()));
        return loanRepository.findAll(specification, pageable)
            .map(LoanEntity::toDomain);
    }

    @Override
    public Optional<Loan> findById(UUID id) {
        return loanRepository.findById(id)
            .map(LoanEntity::toDomain);
    }
    
}

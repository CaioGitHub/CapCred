package com.capcredit.ms_loan.infra.adapters;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.capcredit.ms_loan.application.ports.out.RateRepositoryPort;
import com.capcredit.ms_loan.infra.repositories.RateJpaRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RateRepositoryAdapter implements RateRepositoryPort {

    private final RateJpaRepository repository;

    @Override
    public Optional<BigDecimal> findByTerm(Integer term) {
        return repository.findRateByTermRange(term).or(repository::findMaxRate);
    }
}

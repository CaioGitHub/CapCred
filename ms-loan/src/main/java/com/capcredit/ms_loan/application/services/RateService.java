package com.capcredit.ms_loan.application.services;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.capcredit.ms_loan.application.exceptions.EntityNotFoundException;
import com.capcredit.ms_loan.application.ports.out.RateRepositoryPort;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RateService {

    private final RateRepositoryPort rateRepository;

    public BigDecimal findByTerm(Integer term) {
    return rateRepository.findByTerm(term)
        .orElseThrow(() -> new EntityNotFoundException("Rate not found for term: " + term));
    }
}

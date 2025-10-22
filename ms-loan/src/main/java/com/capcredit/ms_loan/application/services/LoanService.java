package com.capcredit.ms_loan.application.services;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.capcredit.ms_loan.application.exceptions.EntityNotFoundException;
import com.capcredit.ms_loan.application.exceptions.LoanException;
import com.capcredit.ms_loan.application.ports.out.EventPublisherPort;
import com.capcredit.ms_loan.application.ports.out.LoanRepositoryPort;
import com.capcredit.ms_loan.application.ports.out.UserClientPort;
import com.capcredit.ms_loan.domain.events.LoanCreated;
import com.capcredit.ms_loan.domain.model.Loan;
import com.capcredit.ms_loan.domain.model.LoanFilter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoanService {
    private final UserClientPort userClient;
    private final EventPublisherPort eventPublisher;
    private final LoanRepositoryPort loanRepository;

    public void save(Loan loan) {
        if(!loan.isPending()) {
            throw new LoanException("Only loans with PENDING status can be saved");
        }
        loanRepository.save(loan);
        eventPublisher.publishLoanCreated(new LoanCreated(loan));
    }

    public Page<Loan> findAll(LoanFilter filter, Pageable pageable) {
        return loanRepository.findAll(filter, pageable);
    }

    public Loan findById(UUID id) {
        return loanRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Loan not found"));
    }
}


package com.capcredit.ms_loan.application.services;

import org.springframework.stereotype.Service;

import com.capcredit.ms_loan.application.exceptions.LoanException;
import com.capcredit.ms_loan.application.ports.out.EventPublisherPort;
import com.capcredit.ms_loan.application.ports.out.LoanRepositoryPort;
import com.capcredit.ms_loan.application.ports.out.UserClientPort;
import com.capcredit.ms_loan.domain.events.LoanCreated;
import com.capcredit.ms_loan.domain.model.Loan;

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
}


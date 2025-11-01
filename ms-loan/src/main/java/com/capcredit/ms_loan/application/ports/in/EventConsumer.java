package com.capcredit.ms_loan.application.ports.in;

import com.capcredit.ms_loan.domain.events.LoanCompleted;
import com.capcredit.ms_loan.domain.events.LoanCreated;

public interface EventConsumer {
    void processCreatedLoan(LoanCreated loan);
    void processCompletedLoan(LoanCompleted loan);
}

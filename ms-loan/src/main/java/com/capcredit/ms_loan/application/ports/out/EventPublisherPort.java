package com.capcredit.ms_loan.application.ports.out;

import com.capcredit.ms_loan.domain.events.LoanApproved;
import com.capcredit.ms_loan.domain.events.LoanCreated;
import com.capcredit.ms_loan.domain.events.LoanRejected;

public interface EventPublisherPort {
    void publishLoanCreated(LoanCreated loanCreated);
    void publishLoanApproved(LoanApproved loanApproved);
    void publishLoanRejected(LoanRejected loanRejected);
}

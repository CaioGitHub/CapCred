package com.capcredit.ms_loan.domain.model;

import java.util.UUID;

import com.capcredit.ms_loan.domain.enums.LoanStatus;
import com.capcredit.ms_loan.domain.enums.RequestStatus;

public class LoanFilter {
    private UUID userId;
    private LoanStatus loanStatus;
    private RequestStatus requestStatus;

    public UUID getUserId() {
        return userId;
    }
    public void setUserId(UUID userId) {
        this.userId = userId;
    }
    public LoanStatus getLoanStatus() {
        return loanStatus;
    }
    public void setLoanStatus(LoanStatus loanStatus) {
        this.loanStatus = loanStatus;
    }
    public RequestStatus getRequestStatus() {
        return requestStatus;
    }
    public void setRequestStatus(RequestStatus requestStatus) {
        this.requestStatus = requestStatus;
    }
}

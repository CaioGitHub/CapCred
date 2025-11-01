package com.capcredit.payment.port.in;

import com.capcredit.payment.port.in.dtos.LoanApprovedDTO;

public interface LoanPortIn {
    void processLoanApproved(LoanApprovedDTO dto);
}

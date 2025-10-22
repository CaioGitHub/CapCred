package com.capcredit.ms_notification.port.in;

import com.capcredit.ms_notification.port.out.dtos.ApprovedLoanDTO;

public interface ApprovedLoanPortIn {
    void receiveApprovedLoan(ApprovedLoanDTO approvedLoanDTO);
}

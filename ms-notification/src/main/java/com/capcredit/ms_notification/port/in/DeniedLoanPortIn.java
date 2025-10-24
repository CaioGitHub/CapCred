package com.capcredit.ms_notification.port.in;

import com.capcredit.ms_notification.port.out.dtos.DeniedLoanDTO;

public interface DeniedLoanPortIn {
    void receiveDeniedLoan(DeniedLoanDTO deniedLoanDTO);
}

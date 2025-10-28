package com.capcredit.payment.core.service;

import com.capcredit.payment.core.domain.model.Installment;
import com.capcredit.payment.core.domain.model.Loan;
import com.capcredit.payment.port.out.dto.InstallmentDTO;

import java.util.List;
import java.util.UUID;

public interface PaymentService {
   InstallmentDTO processPayment(UUID installmentId);
   void createInstallments(Loan loan);
   List<InstallmentDTO> getInstallmentsByLoanId(UUID loanId);
   Installment findById(UUID installmentId);
}

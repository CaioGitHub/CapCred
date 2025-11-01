package com.capcredit.payment.core.service;

import com.capcredit.payment.core.domain.model.Installment;
import com.capcredit.payment.core.domain.model.Loan;
import com.capcredit.payment.port.out.dto.InstallmentDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface PaymentService {
   InstallmentDTO processPayment(UUID installmentId);
   void createInstallments(Loan loan);
   List<InstallmentDTO> getInstallmentsByLoanId(UUID loanId);
   Installment findById(UUID installmentId);
    Page<InstallmentDTO> getInstallmentsByUserId(UUID userId, Pageable pageable);
    Page<InstallmentDTO> getInstallments(Pageable pageable);}

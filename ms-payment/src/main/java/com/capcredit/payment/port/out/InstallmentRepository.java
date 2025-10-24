package com.capcredit.payment.port.out;

import com.capcredit.payment.core.domain.model.Installment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface InstallmentRepository extends JpaRepository<Installment, UUID> {
    List<Installment> findByLoanId(UUID loanId);

}

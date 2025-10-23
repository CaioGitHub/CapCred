package com.capcredit.payment.core.service;

import com.capcredit.payment.core.domain.model.Installment;
import com.capcredit.payment.core.domain.model.PaymentStatus;
import com.capcredit.payment.port.out.InstallmentRepository;
import com.capcredit.payment.port.out.dto.InstallmentDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class PaymentServiceImpl {
    private final InstallmentRepository installmentRepository;
    public PaymentServiceImpl(InstallmentRepository installmentRepository) {
        this.installmentRepository = installmentRepository;
    }

    public ResponseEntity<InstallmentDTO> processPayment(UUID installmentId){
        Installment paid = installmentRepository.findById(installmentId).orElseThrow(()-> new RuntimeException("Installment not found"));

        if (paid.getPaymentStatus() == PaymentStatus.PAID) {
            throw new RuntimeException("Installment already paid");
        }
        markAsPaid(paid);
        installmentRepository.save(paid);
       InstallmentDTO dto = InstallmentDTO.builder()
               .id(paid.getId())
               .loanId(paid.getLoanId())
               .valueDue(paid.getValueDue())
               .dueDate(paid.getDueDate())
               .paymentDate(paid.getPaymentDate())
               .valuePaid(paid.getValuePaid())
               .paymentStatus(String.valueOf(paid.getPaymentStatus()))
               .build();
        return ResponseEntity.ok(dto);

    }

    private void markAsPaid(Installment paid) {
        paid.setPaymentStatus(PaymentStatus.PAID);
        paid.setPaymentDate(java.time.LocalDateTime.now());
        paid.setValuePaid(paid.getValueDue());
    }

}

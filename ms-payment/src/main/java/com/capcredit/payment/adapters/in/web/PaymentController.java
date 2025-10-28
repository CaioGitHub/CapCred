package com.capcredit.payment.adapters.in.web;

import com.capcredit.payment.core.service.PaymentService;
import com.capcredit.payment.core.service.PaymentServiceImpl;
import com.capcredit.payment.port.in.PaymentPortIn;
import com.capcredit.payment.port.out.dto.InstallmentDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/installment")
public class PaymentController implements PaymentPortIn {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PutMapping("/{installmentId}/pay")
    public ResponseEntity<InstallmentDTO> processPayment(@PathVariable UUID installmentId) {
        return ResponseEntity.ok(paymentService.processPayment(installmentId));
    }

    @GetMapping("/loan/{loanId}")
    public List<InstallmentDTO> getInstallmentsByLoanId(@PathVariable UUID loanId) {
        return paymentService.getInstallmentsByLoanId(loanId);
    }

}


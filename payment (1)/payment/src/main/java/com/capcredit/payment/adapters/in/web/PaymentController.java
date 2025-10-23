package com.capcredit.payment.adapters.in.web;

import com.capcredit.payment.core.service.PaymentServiceImpl;
import com.capcredit.payment.port.out.dto.InstallmentDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/installment")
public class PaymentController {

    private final PaymentServiceImpl paymentService;

    public PaymentController(PaymentServiceImpl paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/{installmentId}/pay")
    public ResponseEntity<InstallmentDTO> payInstallment(@PathVariable UUID installmentId) {
        return paymentService.processPayment(installmentId);
    }
}


package com.capcredit.payment.adapters.in.web;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.capcredit.payment.core.service.PaymentService;
import com.capcredit.payment.port.in.PaymentPortIn;
import com.capcredit.payment.port.out.dto.InstallmentDTO;

@RestController
@RequestMapping("/api/installments")
public class PaymentController implements PaymentPortIn {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PutMapping("/{installmentId}/pay")
    public ResponseEntity<InstallmentDTO> processPayment(@PathVariable UUID installmentId) {
        return ResponseEntity.ok(paymentService.processPayment(installmentId));
    }


}


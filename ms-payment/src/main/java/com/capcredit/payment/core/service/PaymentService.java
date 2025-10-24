package com.capcredit.payment.core.service;

import com.capcredit.payment.port.out.dto.InstallmentDTO;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

public interface PaymentService {
   InstallmentDTO processPayment(UUID installmentId);
}

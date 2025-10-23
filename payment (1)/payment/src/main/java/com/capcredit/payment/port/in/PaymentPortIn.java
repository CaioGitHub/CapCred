package com.capcredit.payment.port.in;

import com.capcredit.payment.port.out.dto.InstallmentDTO;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

public interface PaymentPortIn {
public ResponseEntity<InstallmentDTO> processPayment(UUID installmentId);

}

package com.capcredit.payment.adapters.in.web;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.capcredit.payment.core.domain.model.Loan;
import com.capcredit.payment.core.service.PaymentService;
import com.capcredit.payment.port.in.PaymentPortIn;
import com.capcredit.payment.port.out.dto.InstallmentDTO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/installments")
@RequiredArgsConstructor
@Tag(name = "Loans", description = "Loan management endpoints")
public class PaymentController implements PaymentPortIn {

    private final PaymentService paymentService;

    @PutMapping("/{installmentId}/pay")
    @Operation(summary = "Process payment for an installment")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Loan found", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = Loan.class))
        }),
        @ApiResponse(responseCode = "404", description = "Entity not found", content = @Content),
        @ApiResponse(responseCode = "409", description = "Installment already paid", content = @Content)
    })
    public ResponseEntity<InstallmentDTO> processPayment(@PathVariable UUID installmentId) {
        return ResponseEntity.ok(paymentService.processPayment(installmentId));
    }


}


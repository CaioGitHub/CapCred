package com.capcredit.ms_loan.interfaces.controllers;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.capcredit.ms_loan.application.services.LoanService;
import com.capcredit.ms_loan.application.services.RateService;
import com.capcredit.ms_loan.domain.model.Loan;
import com.capcredit.ms_loan.domain.model.LoanFilter;
import com.capcredit.ms_loan.interfaces.dtos.RequestLoanDTO;
import com.capcredit.ms_loan.interfaces.dtos.ResponseLoanDTO;
import com.capcredit.ms_loan.interfaces.dtos.ResponseSimulatedLoanDTO;
import com.capcredit.ms_loan.interfaces.dtos.SimulateRequestLoanDTO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/loans")
@RequiredArgsConstructor
@Tag(name = "Loans", description = "Loan management endpoints")
public class LoanController {
    
    private final LoanService loanService;
    private final RateService rateService;

    @GetMapping
    @Operation(summary = "Get all loans with optional filtering and pagination")
    public ResponseEntity<Page<Loan>> findAll(LoanFilter filter, Pageable pageable) {
        log.info("Finding loans with filter: {} and pageable: {}", filter, pageable);
        return ResponseEntity.ok(loanService.findAll(filter, pageable));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get loan by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Loan found", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = Loan.class))
        }),
        @ApiResponse(responseCode = "404", description = "Loan not found", content = @Content)
    })
    public ResponseEntity<Loan> findById(@PathVariable UUID id) {
        log.info("Finding loan by id: {}", id);
        return ResponseEntity.ok(loanService.findById(id));
    }

    @PostMapping("/request")
    @Operation(summary = "Create a new loan request")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Loan created", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseLoanDTO.class))
        }),
        @ApiResponse(responseCode = "400", description = "Invalid loan request", content = @Content),
        @ApiResponse(responseCode = "404", description = "Entity not found", content = @Content)
    })
    public ResponseEntity<ResponseLoanDTO> save(@Valid @RequestBody RequestLoanDTO dto) {
        log.info("Creating loan request: {}", dto);
        var rate = rateService.findByTerm(dto.getTermInMonths());
        var loan = dto.toDomain(rate);
        loanService.save(loan);
        return ResponseEntity.status(201).body(ResponseLoanDTO.from(loan));
    }

    @PostMapping("/simulate")
    @Operation(summary = "Simulate a loan without saving it")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Loan simulated", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseLoanDTO.class))
        }),
        @ApiResponse(responseCode = "400", description = "Invalid loan request", content = @Content),
        @ApiResponse(responseCode = "404", description = "Entity not found", content = @Content)
    })
    public ResponseEntity<ResponseSimulatedLoanDTO> simulate(@Valid @RequestBody SimulateRequestLoanDTO dto) {
        log.info("Simulating loan request: {}", dto);
        var rate = rateService.findByTerm(dto.getTermInMonths());
        var loan = dto.toDomain(rate);
        return ResponseEntity.ok(ResponseSimulatedLoanDTO.from(loan));
    }
}

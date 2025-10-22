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

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/api/loans")
@RequiredArgsConstructor
public class LoanController {
    
    private final LoanService loanService;
    private final RateService rateService;

    @GetMapping
    public ResponseEntity<Page<Loan>> findAll(LoanFilter filter, Pageable pageable) {
        return ResponseEntity.ok(loanService.findAll(filter, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Loan> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(loanService.findById(id));
    }

    @PostMapping("/request")
    public ResponseEntity<ResponseLoanDTO> save(@Valid @RequestBody RequestLoanDTO dto) {
        var rate = rateService.findByTerm(dto.getTermInMonths());
        var loan = dto.toDomain(rate);
        loanService.save(loan);
        return ResponseEntity.status(201).body(ResponseLoanDTO.from(loan));
    }

    @PostMapping("/simulate")
    public ResponseEntity<ResponseSimulatedLoanDTO> simulate(@Valid @RequestBody SimulateRequestLoanDTO dto) {
        var rate = rateService.findByTerm(dto.getTermInMonths());
        var loan = dto.toDomain(rate);
        return ResponseEntity.ok(ResponseSimulatedLoanDTO.from(loan));
    }
}

package com.capcredit.ms_loan.domain.model;

import static com.capcredit.ms_loan.domain.enums.LoanStatus.COMPLETED;
import static com.capcredit.ms_loan.domain.enums.RequestStatus.APPROVED;
import static com.capcredit.ms_loan.domain.enums.RequestStatus.PENDING;
import static com.capcredit.ms_loan.domain.enums.RequestStatus.REJECTED;
import static java.math.BigDecimal.ONE;
import static java.math.BigDecimal.ZERO;
import static java.math.RoundingMode.HALF_UP;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import com.capcredit.ms_loan.domain.enums.LoanStatus;
import com.capcredit.ms_loan.domain.enums.RequestStatus;

public class Loan {
    private UUID id;
    private UUID userId;
    private BigDecimal requestedAmount;
    private Integer termInMonths;
    private BigDecimal appliedRate;
    private BigDecimal monthlyInstallmentValue;
    private RequestStatus requestStatus;
    private LoanStatus loanStatus;
    private LocalDate firstDueDate;

    public Loan(
        UUID userId,
        BigDecimal requestedAmount,
        Integer termInMonths,
        BigDecimal appliedRate,
        LocalDate firstDueDate,
        RequestStatus requestStatus,
        LoanStatus loanStatus
    ) {
        this(UUID.randomUUID(), userId, requestedAmount, termInMonths, appliedRate, firstDueDate, requestStatus, loanStatus);
    }

    public Loan(
        UUID id,
        UUID userId,
        BigDecimal requestedAmount,
        Integer termInMonths,
        BigDecimal appliedRate,
        LocalDate firstDueDate,
        RequestStatus requestStatus,
        LoanStatus loanStatus
    ) {
        this.id = id;
        this.userId = userId;
        this.requestStatus = requestStatus;
        this.loanStatus = loanStatus;
        this.requestedAmount = requestedAmount;
        this.termInMonths = termInMonths;
        this.appliedRate = appliedRate;
        this.firstDueDate = firstDueDate;
        this.monthlyInstallmentValue = calculateMonthlyInstallmentValue();
    }

    public UUID getId() {
        return id;
    }

    public UUID getUserId() {
        return userId;
    }

    public BigDecimal getRequestedAmount() {
        return requestedAmount;
    }

    public int getTermInMonths() {
        return termInMonths;
    }

    public BigDecimal getAppliedRate() {
        return appliedRate;
    }

    public LocalDate getFirstDueDate() {
        return firstDueDate;
    }

    public RequestStatus getRequestStatus() {
        return requestStatus;
    }

    public LoanStatus getLoanStatus() {
        return loanStatus;
    }

    public BigDecimal getMonthlyInstallmentValue() {
        return monthlyInstallmentValue;
    }

    public boolean isPending() {
        return PENDING.equals(this.requestStatus);
    }

    public void approve() {
        this.requestStatus = APPROVED;
    }

    public void reject() {
        this.requestStatus = REJECTED;
    }

    public void close() {
        this.loanStatus = COMPLETED;
    }

    /**
     * Calculates the monthly installment value using the Price table formula.
     * 
     * Formula: PMT = [PV × i × (1 + i)^n] / [(1 + i)^n - 1]
     * Where:
     * - PMT (Payment): Monthly installment value
     * - PV (Present Value): Requested loan amount
     * - i (Interest rate): Monthly interest rate (as decimal)
     * - n (Number of periods): Total number of monthly installments
     * 
     * For zero interest rate, the calculation is simply: PV / n
     * 
     * @throws IllegalArgumentException if requestedAmount, appliedRate or termInMonths is null
     * @throws ArithmeticException if termInMonths is zero or negative, or if requestedAmount is zero or negative
     */
    private BigDecimal calculateMonthlyInstallmentValue() {
        if (requestedAmount == null) {
            throw new IllegalArgumentException("Requested amount cannot be null");
        }
        
        if (appliedRate == null) {
            throw new IllegalArgumentException("Applied rate cannot be null");
        }

        if (termInMonths == null) {
            throw new IllegalArgumentException("Term in months cannot be null");
        }
        
        if (termInMonths <= 0) {
            throw new ArithmeticException("Term in months must be positive");
        }
        
        if (requestedAmount.compareTo(ZERO) <= 0) {
            throw new ArithmeticException("Requested amount must be positive");
        }
        
        if (appliedRate.compareTo(ZERO) == 0) {
            return requestedAmount.divide(BigDecimal.valueOf(termInMonths), 5, HALF_UP).setScale(2, HALF_UP);
        }
        
        BigDecimal onePlusRate = ONE.add(appliedRate);
        BigDecimal power = onePlusRate.pow(termInMonths);
        
        BigDecimal numerator = requestedAmount.multiply(appliedRate).multiply(power);
        
        BigDecimal denominator = power.subtract(ONE);
        
        return numerator.divide(denominator, 5, HALF_UP).setScale(2, HALF_UP);
    }
}

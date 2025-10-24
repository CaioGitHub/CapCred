package com.capcredit.ms_loan.domain.models;

import static com.capcredit.ms_loan.domain.enums.LoanStatus.ACTIVE;
import static com.capcredit.ms_loan.domain.enums.RequestStatus.PENDING;
import static java.time.LocalDate.now;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.capcredit.ms_loan.domain.model.Loan;

public class LoanTest {
    
    @Test
    public void shouldCalculateMonthlyInstallmentValue() {
        var loan = new Loan(UUID.randomUUID(), BigDecimal.valueOf(1000), 12, BigDecimal.valueOf(0.05), now(), PENDING, ACTIVE);
        assertEquals(BigDecimal.valueOf(112.83).setScale(2), loan.getMonthlyInstallmentValue());
    }

    @Test
    public void shouldCalculateMonthlyInstallmentValueWhenInterestRateIsZero() {
        var loan = new Loan(UUID.randomUUID(), BigDecimal.valueOf(1000), 12, BigDecimal.ZERO, now(), PENDING, ACTIVE);
        assertEquals(BigDecimal.valueOf(83.33).setScale(2), loan.getMonthlyInstallmentValue());
    }

    @Test
    public void shouldNotCreateLoanWithNullAmountRequested() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Loan(UUID.randomUUID(), null, 12, BigDecimal.valueOf(0.05), now(), PENDING, ACTIVE);
        });
    }

    @Test
    public void shouldNotCreateLoanWithNullTermInMonths() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Loan(UUID.randomUUID(), BigDecimal.valueOf(1000), null, BigDecimal.valueOf(0.05), now(), PENDING, ACTIVE);
        });
    }

    @Test
    public void shouldNotCreateLoanWithNullAppliedRate() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Loan(UUID.randomUUID(), BigDecimal.valueOf(1000), 12, null, now(), PENDING, ACTIVE);
        });
    }

    @Test
    public void shouldNotCreateLoanWhenRequestedAmountIsNotPositive() {
        assertThrows(ArithmeticException.class, () -> {
            new Loan(UUID.randomUUID(), BigDecimal.ZERO, 12, BigDecimal.valueOf(0.05), now(), PENDING, ACTIVE);
        });
        assertThrows(ArithmeticException.class, () -> {
            new Loan(UUID.randomUUID(), BigDecimal.valueOf(-1000), 12, BigDecimal.valueOf(0.05), now(), PENDING, ACTIVE);
        });
    }

    @Test
    public void shouldNotCreateLoanWhenTermInMonthsIsNotPositive() {
        assertThrows(ArithmeticException.class, () -> {
            new Loan(UUID.randomUUID(), BigDecimal.valueOf(1000), 0, BigDecimal.valueOf(0.05), now(), PENDING, ACTIVE);
        });
        assertThrows(ArithmeticException.class, () -> {
            new Loan(UUID.randomUUID(), BigDecimal.valueOf(1000), -12, BigDecimal.valueOf(0.05), now(), PENDING, ACTIVE);
        });
    }
}

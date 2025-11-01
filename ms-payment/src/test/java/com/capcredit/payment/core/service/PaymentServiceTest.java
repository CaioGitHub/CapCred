package com.capcredit.payment.core.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import com.capcredit.payment.core.domain.model.Installment;
import com.capcredit.payment.core.domain.model.Loan;
import com.capcredit.payment.core.domain.model.PaymentStatus;
import com.capcredit.payment.port.out.InstallmentRepository;
import com.capcredit.payment.port.out.RabbitMqSender;

public class PaymentServiceTest {
    
    private PaymentService paymentService;
    private RabbitMqSender rabbitMqSender;
    private InstallmentRepository installmentRepository;
    private UserClient useClient;

    @BeforeEach
    public void setUp() {
        installmentRepository = mock(InstallmentRepository.class);
        rabbitMqSender = mock(RabbitMqSender.class);
        useClient = mock(UserClient.class);
        paymentService = new PaymentServiceImpl(installmentRepository, rabbitMqSender, useClient);
    }

    @Test
    void shouldCreateInstallmentsSuccessfully() {
        var loan = Loan.builder()
            .id(UUID.randomUUID())
            .userId(UUID.randomUUID())
            .monthlyInstallmentValue(new BigDecimal("1500.00"))
            .firstDueDate(LocalDate.of(2025, 2, 15))
            .termInMonths(12)
            .build();

        paymentService.createInstallments(loan);

        var captor = ArgumentCaptor.forClass(List.class);
        verify(installmentRepository, times(1)).saveAll(captor.capture());

        var savedInstallments = captor.getValue();

        assertEquals(loan.getTermInMonths(), savedInstallments.size());

        for (int i = 0; i < loan.getTermInMonths(); i++) {
            var installment = (Installment) savedInstallments.get(i);
            
            assertNotNull(installment.getId());
            assertEquals(loan.getId(), installment.getLoanId());
            assertEquals(loan.getUserId(), installment.getUserId());
            assertEquals(loan.getMonthlyInstallmentValue(), installment.getValueDue());
            assertEquals(loan.getMonthlyInstallmentValue(), installment.getMonthlyInstallmentValue());
            assertEquals(PaymentStatus.PENDING, installment.getPaymentStatus());
            assertEquals(i + 1, installment.getInstallmentNumber());
            assertEquals(loan.getFirstDueDate().plusMonths(i), installment.getDueDate());
            assertNull(installment.getPaymentDate());
            assertNull(installment.getValuePaid());
        }
    }

    @Test
    void shouldCreateInstallmentsWithCorrectDueDates() {
        var loanId = UUID.randomUUID();
        var userId = UUID.randomUUID();
        var monthlyValue = new BigDecimal("500.00");
        var firstDueDate = LocalDate.of(2025, 1, 10);
        var termInMonths = 6;

        var loan = Loan.builder()
                .id(loanId)
                .userId(userId)
                .monthlyInstallmentValue(monthlyValue)
                .firstDueDate(firstDueDate)
                .termInMonths(termInMonths)
                .build();

        paymentService.createInstallments(loan);

        var captor = ArgumentCaptor.forClass(List.class);
        verify(installmentRepository).saveAll(captor.capture());

        var savedInstallments = captor.getValue();

        assertEquals(LocalDate.of(2025, 1, 10), ((Installment) savedInstallments.get(0)).getDueDate());
        assertEquals(LocalDate.of(2025, 2, 10), ((Installment) savedInstallments.get(1)).getDueDate());
        assertEquals(LocalDate.of(2025, 3, 10), ((Installment) savedInstallments.get(2)).getDueDate());
        assertEquals(LocalDate.of(2025, 4, 10), ((Installment) savedInstallments.get(3)).getDueDate());
        assertEquals(LocalDate.of(2025, 5, 10), ((Installment) savedInstallments.get(4)).getDueDate());
        assertEquals(LocalDate.of(2025, 6, 10), ((Installment) savedInstallments.get(5)).getDueDate());
    }

}

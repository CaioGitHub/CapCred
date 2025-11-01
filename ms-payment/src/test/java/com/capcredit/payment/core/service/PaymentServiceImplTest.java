package com.capcredit.payment.core.service;

import com.capcredit.payment.core.domain.model.Installment;
import com.capcredit.payment.core.domain.model.PaymentStatus;
import com.capcredit.payment.core.domain.model.User;
import com.capcredit.payment.core.exception.InstallmentAlreadyPaidException;
import com.capcredit.payment.core.exception.InstallmentNotFoundException;
import com.capcredit.payment.core.exception.UserNotFoundException;
import com.capcredit.payment.port.out.InstallmentRepository;
import com.capcredit.payment.port.out.RabbitMqSender;
import com.capcredit.payment.port.out.dto.InstallmentDTO;
import com.capcredit.payment.port.out.dto.LoanCompletedDTO;
import com.capcredit.payment.port.out.dto.PaymentReceivedDTO;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PaymentServiceImplTest {

    @Mock
    private InstallmentRepository installmentRepository;

    @Mock
    private RabbitMqSender rabbitMqSender;

    @Mock
    private UserClient userClient;

    @InjectMocks
    private PaymentServiceImpl paymentService;

    private UUID installmentId;
    private Installment unpaidInstallment;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        installmentId = UUID.randomUUID();
        unpaidInstallment = Installment.builder()
                .id(installmentId)
                .loanId(UUID.randomUUID())
                .valueDue(BigDecimal.valueOf(1000))
                .dueDate(LocalDate.now().minusDays(5))
                .paymentStatus(PaymentStatus.PENDING)
                .userId(UUID.randomUUID())
                .build();
    }

    @Test
    void shouldProcessPaymentSuccessfully() {
        var user = new User(unpaidInstallment.getUserId(), "Test User", "test@example.com", "+00 (00) 0 0000-0000");
        when(userClient.findById(any(UUID.class))).thenReturn(Optional.of(user));
        when(installmentRepository.findById(installmentId)).thenReturn(Optional.of(unpaidInstallment));
        when(installmentRepository.save(any())).thenReturn(unpaidInstallment);
        when(installmentRepository.existsByLoanIdAndPaymentStatus(any(UUID.class), any(PaymentStatus.class))).thenReturn(false);

        InstallmentDTO result = paymentService.processPayment(installmentId);

        assertNotNull(result);
        assertEquals(PaymentStatus.PAID.name(), result.paymentStatus());
        assertNotNull(result.paymentDate());

        verify(installmentRepository).save(unpaidInstallment);

        verify(rabbitMqSender, atLeastOnce()).sendPaymentEvent(any(PaymentReceivedDTO.class));
        verify(rabbitMqSender, times(1)).sendLoanCompletedEvent(any(LoanCompletedDTO.class));
    }

    @Test
    void shouldNotPublishLoanCompletedWhenExistsPendingInstallments() {
        var user = new User(unpaidInstallment.getUserId(), "Test User", "test@example.com", "+00 (00) 0 0000-0000");
        when(userClient.findById(any(UUID.class))).thenReturn(Optional.of(user));
        when(installmentRepository.findById(installmentId)).thenReturn(Optional.of(unpaidInstallment));
        when(installmentRepository.save(any())).thenReturn(unpaidInstallment);
        when(installmentRepository.existsByLoanIdAndPaymentStatus(any(UUID.class), any(PaymentStatus.class))).thenReturn(true);

        InstallmentDTO result = paymentService.processPayment(installmentId);

        assertNotNull(result);
        assertEquals(PaymentStatus.PAID.name(), result.paymentStatus());
        assertNotNull(result.paymentDate());

        verify(installmentRepository).save(unpaidInstallment);

        verify(rabbitMqSender, atLeastOnce()).sendPaymentEvent(any(PaymentReceivedDTO.class));
        verify(rabbitMqSender, never()).sendLoanCompletedEvent(any(LoanCompletedDTO.class));
    }

    @Test
    void shouldThrowUserNotFoundExceptionWhenNotFoundUserOnCompleteLoan() {
        when(installmentRepository.findById(installmentId)).thenReturn(Optional.of(unpaidInstallment));
        when(installmentRepository.save(any())).thenReturn(unpaidInstallment);
        when(installmentRepository.existsByLoanIdAndPaymentStatus(any(UUID.class), any(PaymentStatus.class))).thenReturn(false);
        when(userClient.findById(any(UUID.class))).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> paymentService.processPayment(installmentId));
    }

    @Test
    void shouldThrowExceptionWhenInstallmentNotFound() {
        when(installmentRepository.findById(installmentId)).thenReturn(Optional.empty());

        assertThrows(InstallmentNotFoundException.class, () -> paymentService.processPayment(installmentId));

        verify(installmentRepository, never()).save(any());
        verify(rabbitMqSender, never()).sendPaymentEvent(any());
    }

    @Test
    void shouldThrowExceptionWhenInstallmentAlreadyPaid() {
        unpaidInstallment.setPaymentStatus(PaymentStatus.PAID);
        when(installmentRepository.findById(installmentId)).thenReturn(Optional.of(unpaidInstallment));

        assertThrows(InstallmentAlreadyPaidException.class, () -> paymentService.processPayment(installmentId));

        verify(installmentRepository, never()).save(any());
        verify(rabbitMqSender, never()).sendPaymentEvent(any());
    }

    @Test
    void shouldReturnInstallmentsByLoanId() {
        UUID loanId = UUID.randomUUID();
        when(installmentRepository.findByLoanId(loanId)).thenReturn(List.of(unpaidInstallment));

        var installments = paymentService.getInstallmentsByLoanId(loanId);

        assertEquals(1, installments.size());
        assertEquals(unpaidInstallment.getId(), installments.get(0).installmentId());
    }
}

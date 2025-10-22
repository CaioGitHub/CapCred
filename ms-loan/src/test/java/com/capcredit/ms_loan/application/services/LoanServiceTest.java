package com.capcredit.ms_loan.application.services;

import static com.capcredit.ms_loan.domain.enums.LoanStatus.ACTIVE;
import static com.capcredit.ms_loan.domain.enums.RequestStatus.PENDING;
import static java.time.LocalDate.now;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.capcredit.ms_loan.application.exceptions.EntityNotFoundException;
import com.capcredit.ms_loan.application.exceptions.LoanException;
import com.capcredit.ms_loan.application.ports.out.EventPublisherPort;
import com.capcredit.ms_loan.application.ports.out.LoanRepositoryPort;
import com.capcredit.ms_loan.application.ports.out.UserClientPort;
import com.capcredit.ms_loan.domain.enums.RequestStatus;
import com.capcredit.ms_loan.domain.events.LoanCreated;
import com.capcredit.ms_loan.domain.model.Loan;

public class LoanServiceTest {

    private LoanService loanService;
    private UserClientPort userClient;
    private EventPublisherPort eventPublisher;
    private LoanRepositoryPort loanRepository;

    @BeforeEach
    public void setup() {
        userClient = mock(UserClientPort.class);
        eventPublisher = mock(EventPublisherPort.class);
        loanRepository = mock(LoanRepositoryPort.class);
        loanService = new LoanService(userClient, eventPublisher, loanRepository);
    }

    @Test
    public void shouldSaveLoanAndPublishEvent() {
        var loan = new Loan(UUID.randomUUID(), BigDecimal.TEN, 12, BigDecimal.valueOf(5), now(), PENDING, ACTIVE);
        
        loanService.save(loan);

        verify(loanRepository).save(any(Loan.class));
        verify(eventPublisher).publishLoanCreated(any(LoanCreated.class));
    }

    @Test
    public void shouldNotSaveLoanAndPublishEventWhenLoanIsNotPending() {
        RequestStatus.notPendingStatus().forEach(status -> {
            var loan = new Loan(UUID.randomUUID(), BigDecimal.TEN, 12, BigDecimal.valueOf(5), now(), status, ACTIVE);
            assertThrows(LoanException.class, () -> {
                loanService.save(loan);
            });
        });
    }

    @Test
    public void shouldReturnLoanWhenFindById() {
        var loanId = UUID.randomUUID();
        var loan = new Loan(loanId, BigDecimal.TEN, 12, BigDecimal.valueOf(5), now(), PENDING, ACTIVE);

        when(loanRepository.findById(any(UUID.class))).thenReturn(Optional.of(loan));

        var result = loanService.findById(loanId);

        assertEquals(loan, result);
    }


    @Test
    public void shouldNotReturnLoanWhenNotFound() {
        var loanId = UUID.randomUUID();

        when(loanRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> {
            loanService.findById(loanId);
        });
    }
}

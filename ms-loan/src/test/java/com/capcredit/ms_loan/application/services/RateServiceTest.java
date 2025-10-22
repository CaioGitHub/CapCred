package com.capcredit.ms_loan.application.services;

import static java.math.BigDecimal.TEN;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.capcredit.ms_loan.application.exceptions.EntityNotFoundException;
import com.capcredit.ms_loan.application.ports.out.RateRepositoryPort;


public class RateServiceTest {
    private RateService rateService;
    private RateRepositoryPort rateRepository;

    @BeforeEach
    public void setup() {
        rateRepository = mock(RateRepositoryPort.class);
        rateService = new RateService(rateRepository);
    }

    @Test
    public void shouldReturnRateWhenFindById() {
        when(rateRepository.findByTerm(any(Integer.class))).thenReturn(Optional.of(TEN));

        var result = rateService.findByTerm(12);

        assertEquals(TEN, result);
    }


    @Test
    public void shouldNotReturnRateWhenNotFound() {
        when(rateRepository.findByTerm(any(Integer.class))).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> {
            rateService.findByTerm(12);
        });
    }
}

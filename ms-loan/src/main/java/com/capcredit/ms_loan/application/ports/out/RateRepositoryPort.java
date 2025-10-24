package com.capcredit.ms_loan.application.ports.out;

import java.math.BigDecimal;
import java.util.Optional;

public interface RateRepositoryPort {
    Optional<BigDecimal> findByTerm(Integer term);
}

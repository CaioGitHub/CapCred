package com.capcredit.ms_loan.infra.repositories;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.capcredit.ms_loan.infra.entities.RateEntity;

public interface RateJpaRepository extends JpaRepository<RateEntity, UUID>, JpaSpecificationExecutor<RateEntity> {
    
    @Query("SELECT r.value FROM RateEntity r WHERE r.minTerm <= :term AND :term <= r.maxTerm")
    Optional<BigDecimal> findRateByTermRange(@Param("term") Integer term);

    @Query("SELECT MAX(r.value) FROM RateEntity r")
    Optional<BigDecimal> findMaxRate();
}

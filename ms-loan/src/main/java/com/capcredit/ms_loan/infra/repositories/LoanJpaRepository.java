package com.capcredit.ms_loan.infra.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.capcredit.ms_loan.infra.entities.LoanEntity;

public interface LoanJpaRepository extends JpaRepository<LoanEntity, UUID>, JpaSpecificationExecutor<LoanEntity> {}

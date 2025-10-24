package com.capcredit.ms_loan.infra.entities;

import java.math.BigDecimal;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "rate")
@NoArgsConstructor
@AllArgsConstructor
public class RateEntity {
    @Id
    private UUID id;

    @Column(name = "min_term",  nullable = false)
    private Integer minTerm;

    @Column(name = "max_term",  nullable = false)
    private Integer maxTerm;

    @Column(name = "rate_value", nullable = false)
    private BigDecimal value;
}

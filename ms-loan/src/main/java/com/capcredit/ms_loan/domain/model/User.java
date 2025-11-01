package com.capcredit.ms_loan.domain.model;

import java.math.BigDecimal;
import java.util.UUID;

public class User {
    private UUID id;
    private String name;
    private String cpf;
    private String email;
    private String phone;
    private BigDecimal monthlyIncome;

    public User(UUID id, String name, String cpf, String email, String phone, BigDecimal monthlyIncome) {
        this.id = id;
        this.name = name;
        this.cpf = cpf;
        this.email = email;
        this.phone = phone;
        this.monthlyIncome = monthlyIncome;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCpf() {
        return cpf;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public BigDecimal getMonthlyIncome() {
        return monthlyIncome;
    }
}

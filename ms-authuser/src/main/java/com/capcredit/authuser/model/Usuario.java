package com.capcredit.authuser.model;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "usuarios")
@Data

public class Usuario {

    @Id
    @GeneratedValue
    private UUID id;

    private String name;
    private String cpf;


    @Column(unique = true)
    private String email;


    @Column(name = "password_hash")
    private String passwordHash;

    @Column(name = "monthly_income", precision = 38, scale = 2)
    private BigDecimal monthlyIncome;

    @Enumerated(EnumType.STRING)
    private Role role;

    public enum Role {
        ADMIN,
        CLIENT
    }
}

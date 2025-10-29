package com.capcredit.authuser.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequestDto {
    private String name;
    private String cpf;
    private String email;
    private String senha;
    private BigDecimal monthlyIncome;
}


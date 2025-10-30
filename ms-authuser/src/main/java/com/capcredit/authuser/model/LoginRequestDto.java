package com.capcredit.authuser.model;


import lombok.Data;

@Data
public class LoginRequestDto {
    private String email;
    private String senha;


}


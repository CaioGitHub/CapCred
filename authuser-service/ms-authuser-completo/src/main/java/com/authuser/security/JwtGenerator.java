package com.authuser.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.security.PrivateKey;
import java.util.Date;

public class JwtGenerator {

    public static String generateToken(PrivateKey privateKey) {
        return Jwts.builder()
                .setSubject("leticia")
                .setIssuer("ms-authuser")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 3600000)) // 1 hora
                .signWith(privateKey, SignatureAlgorithm.RS256)
                .compact();
    }
}
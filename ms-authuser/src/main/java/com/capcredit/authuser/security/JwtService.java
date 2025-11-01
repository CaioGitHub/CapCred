package com.capcredit.authuser.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Service;

import java.util.Date;


import io.jsonwebtoken.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.util.Collections;
import java.util.UUID;

@Service
public class JwtService {

    private final RsaKeyProvider rsaKeyProvider;

    public JwtService(RsaKeyProvider rsaKeyProvider) {
        this.rsaKeyProvider = rsaKeyProvider;
    }

    // Gera token JWT assinado com chave privada RSA
    public String generateToken(UUID userId, String email, String role) {
        return Jwts.builder()
                .setSubject(email)
                .claim("role", role)
                .claim("userId", userId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 3600000)) // 1 hora
                .signWith(rsaKeyProvider.getPrivateKey(), SignatureAlgorithm.RS256)
                .compact();
    }

    // Valida token usando chave pública RSA
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(rsaKeyProvider.getPublicKey())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }

    // Extrai username do token
    public String extractUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(rsaKeyProvider.getPublicKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    // Cria objeto Authentication para o Spring Security
    public Authentication getAuthentication(String token) {
        String username = extractUsername(token);
        return new UsernamePasswordAuthenticationToken(username, null, Collections.emptyList());
    }
}
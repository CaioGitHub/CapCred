package com.capcredit.authuser;

import com.capcredit.authuser.security.JwtGenerator;
import com.capcredit.authuser.security.JwtValidator;
import com.capcredit.authuser.security.KeyUtils;
import io.jsonwebtoken.Claims;

import java.security.PrivateKey;
import java.security.PublicKey;

public class JwtTest {
    public static void main(String[] args) throws Exception {
        // Carregar as chaves
        PrivateKey privateKey = KeyUtils.getPrivateKey("keys/private.pem");
        PublicKey publicKey = KeyUtils.getPublicKey("keys/public.pem");

        // Gerar token
        String token = JwtGenerator.generateToken(privateKey);
        System.out.println("Token JWT: " + token);

        // Validar token
        Claims claims = JwtValidator.validateToken(token, publicKey);
        System.out.println("Subject: " + claims.getSubject());
        System.out.println("Issuer: " + claims.getIssuer());
        System.out.println("Expiração: " + claims.getExpiration());
    }
}

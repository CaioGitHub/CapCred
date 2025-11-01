package com.capcredit.authuser.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.security.PrivateKey;
import java.util.Date;

public class JwtGenerator {

    public static String generateToken(PrivateKey privateKey) {
        return Jwts.builder()
                .setSubject("capcred-app")
                .setIssuer("ms-authuser")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 3600000))
                .signWith(privateKey, SignatureAlgorithm.RS256)
                .compact();
    }
}
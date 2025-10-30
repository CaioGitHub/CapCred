package com.capcredit.authuser.security;


import org.springframework.stereotype.Component;


import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;


import org.springframework.beans.factory.annotation.Value;

import java.security.PublicKey;

import java.security.spec.X509EncodedKeySpec;








import org.springframework.stereotype.Component;


import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;


import org.springframework.beans.factory.annotation.Value;

import java.security.PublicKey;

import java.security.spec.X509EncodedKeySpec;



@Component
public class RsaKeyProvider {

    private final PrivateKey privateKey;
    private final PublicKey publicKey;

    public RsaKeyProvider(
            @Value("${rsa.private-key}") String privateKeyPath,
            @Value("${rsa.public-key}") String publicKeyPath,
            org.springframework.core.io.ResourceLoader resourceLoader) {
        try {
            this.privateKey = loadPrivateKey(resourceLoader.getResource("classpath:" + privateKeyPath));
            this.publicKey = loadPublicKey(resourceLoader.getResource("classpath:" + publicKeyPath));
        } catch (Exception e) {
            throw new RuntimeException("Erro ao carregar chaves RSA", e);
        }
    }

    private PrivateKey loadPrivateKey(org.springframework.core.io.Resource resource) throws Exception {
        String key = new String(resource.getInputStream().readAllBytes())
                .replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replaceAll("\\s", "");
        byte[] decoded = Base64.getDecoder().decode(key);
        return KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(decoded));
    }

    private PublicKey loadPublicKey(org.springframework.core.io.Resource resource) throws Exception {
        String key = new String(resource.getInputStream().readAllBytes())
                .replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "")
                .replaceAll("\\s", "");
        byte[] decoded = Base64.getDecoder().decode(key);
        return KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(decoded));
    }

    public PrivateKey getPrivateKey() { return privateKey; }
    public PublicKey getPublicKey() { return publicKey; }
}
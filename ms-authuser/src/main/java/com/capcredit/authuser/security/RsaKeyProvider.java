package com.capcredit.authuser.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * Provedor de chaves RSA para JWT.
 * Carrega chaves privadas e públicas das variáveis de ambiente.
 */
@Component
public class RsaKeyProvider {

    private final PrivateKey privateKey;
    private final PublicKey publicKey;

    public RsaKeyProvider(
            @Value("${JWT_PRIVATE_KEY}") String privateKeyBase64,
            @Value("${JWT_PUBLIC_KEY}") String publicKeyBase64) {
        try {
            this.privateKey = loadPrivateKeyFromBase64(privateKeyBase64);
            this.publicKey = loadPublicKeyFromBase64(publicKeyBase64);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao carregar chaves RSA das variáveis de ambiente", e);
        }
    }

    private PrivateKey loadPrivateKeyFromBase64(String keyBase64) throws Exception {
        byte[] decoded = Base64.getDecoder().decode(keyBase64);
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(decoded);
        return KeyFactory.getInstance("RSA").generatePrivate(spec);
    }

    private PublicKey loadPublicKeyFromBase64(String keyBase64) throws Exception {
        byte[] decoded = Base64.getDecoder().decode(keyBase64);
        X509EncodedKeySpec spec = new X509EncodedKeySpec(decoded);
        return KeyFactory.getInstance("RSA").generatePublic(spec);
    }

    public PrivateKey getPrivateKey() {
        return privateKey;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }
}

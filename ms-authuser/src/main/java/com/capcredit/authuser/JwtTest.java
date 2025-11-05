package com.capcredit.authuser;

import com.capcredit.authuser.security.JwtGenerator;
import com.capcredit.authuser.security.JwtValidator;
import io.jsonwebtoken.Claims;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * Classe de teste para JWT.
 * IMPORTANTE: As chaves agora devem estar configuradas nas variáveis de ambiente:
 * - JWT_PRIVATE_KEY
 * - JWT_PUBLIC_KEY
 */
public class JwtTest {
    public static void main(String[] args) throws Exception {
        // Carregar as chaves das variáveis de ambiente
        String privateKeyBase64 = System.getenv("JWT_PRIVATE_KEY");
        String publicKeyBase64 = System.getenv("JWT_PUBLIC_KEY");

        if (privateKeyBase64 == null || publicKeyBase64 == null) {
            System.err.println("ERRO: Variáveis de ambiente JWT_PRIVATE_KEY e JWT_PUBLIC_KEY não configuradas!");
            System.err.println("Configure-as no arquivo .env ou nas variáveis de ambiente do sistema.");
            System.exit(1);
        }

        PrivateKey privateKey = loadPrivateKey(privateKeyBase64);
        PublicKey publicKey = loadPublicKey(publicKeyBase64);

        // Gerar token
        String token = JwtGenerator.generateToken(privateKey);
        System.out.println("Token JWT: " + token);

        // Validar token
        Claims claims = JwtValidator.validateToken(token, publicKey);
        System.out.println("Subject: " + claims.getSubject());
        System.out.println("Issuer: " + claims.getIssuer());
        System.out.println("Expiração: " + claims.getExpiration());
    }

    private static PrivateKey loadPrivateKey(String keyBase64) throws Exception {
        byte[] decoded = Base64.getDecoder().decode(keyBase64);
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(decoded);
        return KeyFactory.getInstance("RSA").generatePrivate(spec);
    }

    private static PublicKey loadPublicKey(String keyBase64) throws Exception {
        byte[] decoded = Base64.getDecoder().decode(keyBase64);
        X509EncodedKeySpec spec = new X509EncodedKeySpec(decoded);
        return KeyFactory.getInstance("RSA").generatePublic(spec);
    }
}

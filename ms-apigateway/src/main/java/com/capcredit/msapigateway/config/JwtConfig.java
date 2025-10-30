package com.capcredit.msapigateway.config;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuração para carregamento da chave pública RSA.
 * A chave pública é necessária para validar a assinatura dos tokens JWT (RS256) gerados pelo MS-AUTHUSER.
 */
@Configuration
public class JwtConfig {

    @Value("${jwt.public-key}")
    private String jwtPublicKey;

    /**
     * Cria e retorna o objeto PublicKey a partir da string Base64 da chave pública.
     * @return PublicKey utilizada para validação do JWT.
     * @throws RuntimeException se a chave for inválida ou o algoritmo não for suportado.
     */
    @Bean
    public PublicKey publicKey() {
        try {
            String publicKeyPEM = jwtPublicKey
                    .replace("-----BEGIN PUBLIC KEY-----", "")
                    .replace("-----END PUBLIC KEY-----", "")
                    .replaceAll("\\s", "");

            byte[] keyBytes = Base64.getDecoder().decode(publicKeyPEM);

            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");

            return keyFactory.generatePublic(keySpec);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Algoritmo RSA não suportado.", e);
        } catch (InvalidKeySpecException e) {
            throw new RuntimeException("Especificação de chave pública inválida (provavelmente formato errado).", e);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Chave pública JWT não está em formato Base64 válido.", e);
        }
    }
}

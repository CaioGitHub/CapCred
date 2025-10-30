package com.capcredit.msapigateway.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.SignatureException;
import io.jsonwebtoken.ExpiredJwtException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.security.PublicKey;
import java.util.Objects;

/**
 * Filtro Global para validação do JWT (RS256) e injeção dos headers de usuário.
 * Este filtro é aplicado a todas as rotas protegidas.
 */
@Component
public class AuthGlobalFilter implements GlobalFilter, Ordered {

    private static final String BEARER_PREFIX = "Bearer ";
    private static final String X_USER_ID = "X-User-ID";
    private static final String X_USER_ROLE = "X-User-ROLE";
    private static final String USER_ID_CLAIM = "userId";
    private static final String ROLE_CLAIM = "role";

    private final JwtParser jwtParser;

    @Autowired
    public AuthGlobalFilter(PublicKey publicKey) {
        this.jwtParser = Jwts.parser().setSigningKey(publicKey).build();
    }

    /**
     * Lógica principal de filtragem.
     * @param exchange O contexto reativo da requisição/resposta.
     * @param chain A cadeia de filtros.
     * @return Mono<Void> indicando a continuação do fluxo.
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath();

        if (path.startsWith("/api/auth")) {
            return chain.filter(exchange);
        }

        String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith(BEARER_PREFIX)) {
            return this.onError(exchange, "Token JWT ausente ou mal formatado", HttpStatus.UNAUTHORIZED);
        }

        String token = authHeader.substring(BEARER_PREFIX.length());

        try {
            Jws<Claims> claimsJws = jwtParser.parseClaimsJws(token);
            Claims claims = claimsJws.getBody();

            String userId = claims.get(USER_ID_CLAIM, String.class);
            String role = claims.get(ROLE_CLAIM, String.class);

            if (Objects.isNull(userId) || Objects.isNull(role)) {
                return this.onError(exchange, "Claims essenciais (userId, role) não encontradas no token", HttpStatus.FORBIDDEN);
            }

            ServerHttpRequest modifiedRequest = request.mutate()
                    .header(X_USER_ID, userId)
                    .header(X_USER_ROLE, role)
                    .build();

            return chain.filter(exchange.mutate().request(modifiedRequest).build());

        } catch (SignatureException e) {
            return this.onError(exchange, "Assinatura JWT inválida.", HttpStatus.FORBIDDEN);
        } catch (ExpiredJwtException e) {
            return this.onError(exchange, "Token JWT expirado.", HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            return this.onError(exchange, "Falha ao processar o token JWT.", HttpStatus.UNAUTHORIZED);
        }
    }

    /**
     * Método utilitário para tratar erros de autenticação reativamente.
     */
    private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus httpStatus) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);
        // Adiciona um header WWW-Authenticate para indicar o erro
        response.getHeaders().add("WWW-Authenticate", "Bearer realm=\"capcredit\", error=\"" + err + "\"");
        return response.setComplete();
    }

    /**
     * Garante que este filtro seja executado no início da cadeia de filtros do Gateway.
     */
    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}

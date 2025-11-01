package com.capcredit.msapigateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import java.util.List;

/**
 * Configuração de segurança reativa para o API Gateway.
 * Define que a segurança deve ser stateless (sem estado) e desabilita CSRF, FormLogin e HttpBasic.
 * NOTA: A validação real do JWT e a propagação dos headers são feitas no filtro customizado AuthGlobalFilter.
 */
@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    private static final String[] SWAGGER_RESOURCES = {
            "/swagger-ui.html",
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/webjars/**",
            "/actuator/**"
    };

    /**
     * Define a cadeia de filtros de segurança para requisições HTTP (WebFlux Security Chain).
     * Utiliza a sintaxe moderna de expressões lambda para desabilitar as configurações depreciadas.
     * * @param http Objeto para configurar a segurança HTTP reativa.
     * @return A cadeia de filtros de segurança configurada.
     */
    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {

        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
                .authorizeExchange(exchanges -> exchanges
                        .pathMatchers("/api/auth/**").permitAll()
                        .pathMatchers("/api/users**").permitAll()
                        .pathMatchers("/api/loans/**").permitAll()
                        .pathMatchers("/api/installments/**").permitAll()
                        .pathMatchers(SWAGGER_RESOURCES).permitAll()
                        .anyExchange().authenticated()
                )
                .build();
    }

    /**
     * Configura o CorsWebFilter para permitir requisições do frontend Angular.
     * @return CorsWebFilter configurado.
     */
    @Bean
    public CorsWebFilter corsWebFilter() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:4200"));
        config.setAllowedMethods(List.of("GET","POST","PUT","DELETE","OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);
        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return new CorsWebFilter(source);
    }
}

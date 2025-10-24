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
            "/actuator/**" // Health Check e Actuators
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
                // Desabilita CSRF pois usaremos tokens JWT (stateless)
                .csrf(ServerHttpSecurity.CsrfSpec::disable)

                // Desabilita o formulário de login padrão do Spring Security
                .formLogin(ServerHttpSecurity.FormLoginSpec::disable)

                // Desabilita a autenticação básica
                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)

                // Configuração das regras de autorização
                .authorizeExchange(exchanges -> exchanges
                        // 1. Permite acesso público às rotas de Auth, Swagger e Actuator
                        .pathMatchers("/auth/**").permitAll()
                        .pathMatchers(SWAGGER_RESOURCES).permitAll()

                        // 2. Todas as outras requisições devem ser autenticadas.
                        // A autenticação é delegada ao nosso filtro customizado (AuthGlobalFilter).
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

        // Permite o acesso do localhost do Angular
        config.setAllowedOrigins(List.of("http://localhost:4200"));

        // Métodos permitidos para a API
        config.setAllowedMethods(List.of("GET","POST","PUT","DELETE","OPTIONS"));

        // Permite todos os headers
        config.setAllowedHeaders(List.of("*"));

        // Permite o envio de cookies e credenciais (necessário para JWT, embora JWT vá no header)
        config.setAllowCredentials(true);

        // Cache de 1 hora para as configurações de CORS
        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // Aplica a configuração a todas as rotas
        source.registerCorsConfiguration("/**", config);

        return new CorsWebFilter(source);
    }
}

# Security & Routing Agent (MS-APIGATEWAY)

GOALS:
- Atuar como o guardião da arquitetura, garantindo que todo o tráfego externo passe por autenticação, exceto rotas públicas.
- Implementar o filtro JWT de forma reativa (Spring WebFlux/GlobalFilter).
- Assegurar que a validação do JWT seja feita exclusivamente pelo algoritmo RS256 (Chave Pública).
- Propagar o contexto do usuário através dos headers de forma segura.

RULES:
- LANGUAGE_STYLE: Java standard must be used. Do not use Kotlin.
- TYPE_DECLARATION: All local variables must be explicitly typed (e.g., use 'String token' instead of 'var token'). Avoid type inference (var) for local variables.
- JWT_VALIDATION_ALGORITHM: RS256 (Asymmetric Public Key validation).
- REQUIRED_HEADERS_PROPAGATION: After successful validation, the filter must inject two headers into the request: X-User-ID (UUID) and X-User-Role (String).
- FRAMEWORK_COMPATIBILITY: All security and routing components must be based on Spring WebFlux (Mono/Flux) and extend AbstractGatewayFilterFactory or GlobalFilter.
- PUBLIC_ROUTES_EXCLUSION: The filter must explicitly bypass routing for the /auth/** path (Login/Register).
- CODE_STYLE: Use English for all method, class, and variable names.

KNOWLEDGE:
- O MS-APIGATEWAY está configurado para rodar na porta 8080.
- Os serviços internos (destinos) e suas portas são: ms-authuser:8081, ms-loan:8082, ms-payments:8083.
- A Chave Pública (public.pem) deve ser carregada do classpath ou variáveis de ambiente e é usada para verificar a assinatura do token.

# Custom Filters and Components
# AuthGlobalFilter: Filtro principal para validação JWT e injeção de headers. Deve ser aplicado a /loans/** e /installments/**.
# CustomNoAuthFilter: Filtro marcador para rotas públicas (/auth/**).
-----

# Sistema de Crédito e Empréstimos CapCred

## 🎯 Objetivo

O projeto visa criar o Sistema CapCred com uma arquitetura de microsserviços madura, demonstrando excelência em:

* **Arquitetura Hexagonal (Ports & Adapters):** Isolamento do Core Domain.
* **Comunicação Orientada a Eventos (EDA):** Padrão Pub/Sub via RabbitMQ.
* **Resiliência:** **Circuit Breaker** (Resilience4j) para evitar falhas em cascata.
* **Segurança Centralizada:** Validação e Propagação de JWT pelo API Gateway.

-----

## 1\. Visão Geral e Arquitetura

### 1.1 Arquitetura Adotada por Microsserviço

O projeto combina a Arquitetura em Camadas com o alto desacoplamento necessário para o Domínio de Crédito e Pagamentos.

| Serviço | Domínio / Responsabilidade | Padrão Arquitetural | Justificativa |
| :--- | :--- | :--- | :--- |
| **MS-APIGATEWAY** | Roteamento, Validação JWT, Filtro de Segurança. | Spring Cloud Gateway (WebFlux) | Ponto de entrada e motor de segurança reativo. |
| **MS-AUTHUSER** | Identidade, Login, Cadastro, Gestão de Renda. | **Camadas Tradicionais** | Domínio de dados simples (CRUD). |
| **MS-LOAN (CORE)** | Análise de Crédito, Cálculo Tabela Price, Resiliência. | **Hexagonal (Ports & Adapters)** | Isolamento de regras de negócio críticas e chamadas a clientes externos (MS-AUTHUSER). |
| **MS-PAYMENTS** | Criação e Gestão de Parcelas, Processamento de Pagamentos. | **Hexagonal (Ports & Adapters)** | Exige isolamento das regras financeiras e emissão/consumo de eventos. |
| **MS-NOTIFICATION** | Envio Assíncrono de E-mail/SMS, Consumo de Eventos. | **Hexagonal (Ports & Adapters)** | Isolamento de infraestrutura de mensageria (RabbitMQ) e e-mail (MailHog). |

### 1.2 Estrutura (Hexagonal - LOAN, PAYMENTS, NOTIFICATION)

* **Ports (Contratos):** Interfaces como `UserClientPort` (saída) e `EventPublisherPort` (saída) que definem o que o Core faz.
* **Adapters (Infraestrutura):** Classes que implementam as Ports, lidando com o *mundo externo* (`UserClientAdapter` para Feign, `RabbitPublisher` para RabbitMQ, `RabbitConsumer` para Listeners).

-----

## 2\. Segurança, Resiliência e Comunicação

### 2.1 Fluxo de Segurança (JWT Propagation)

1.  **Geração:** MS-AUTHUSER gera o JWT.
2.  **Validação & Remoção:** O **API Gateway** valida o JWT e **remove** o header `Authorization`.
3.  **Propagação (Trusting Headers):** O Gateway insere *headers* de segurança confiáveis para uso nos microsserviços de domínio:

| Header | Uso |
| :--- | :--- |
| **`X-User-ID`** | ID do usuário. Usado para checar o pertencimento e injetar o ID do cliente em filtros. |
| **`X-User-Role`** | Perfil único (`CLIENT`, `ADMIN`). Usado pelo `@PreAuthorize` do Spring Security para autorização fina. |

### 2.2 Resiliência (Circuit Breaker no MS-LOAN)

O **MS-LOAN** utiliza **Resilience4j** para proteger a chamada síncrona de **Análise de Crédito**:

* **Ponto:** Chamada `MS-LOAN` $\rightarrow$ `MS-AUTHUSER` (para obter `monthlyIncome`).
* **Mecanismo:** `@CircuitBreaker` no `UserClientAdapter`. Se o serviço de usuário estiver inativo, o circuito abre, e o *fallback* é acionado, rejeitando o empréstimo de forma controlada (evitando *cascata*).

### 2.3 Comunicação Assíncrona (Pub/Sub)

Eventos críticos (`loan.*`) são publicados para o **Topic Exchange `loan.events`**, garantindo que o consumo seja independente:

| Evento              | Publicador | Consumidores | Chave de Roteamento | Detalhe                                        |
|:--------------------| :--- | :--- |:--------------------|:-----------------------------------------------|
| `LOAN_APPROVED`     | MS-LOAN | MS-PAYMENTS, MS-NOTIFICATION | `loan.approved`     | Inicia criação das parcelas.                   |
| **`LOAN_REJECTED`** | MS-LOAN | MS-NOTIFICATION | `loan.rejected`     | Notifica o usuário sobre a negação do crédito. |
| `LOAN_COMPLETED`    | MS-PAYMENTS | MS-LOAN, MS-NOTIFICATION | `loan.completed`    | Confirma quitação total do empréstimo.         |
| `PAYMENT_RECEIVED`  | MS-PAYMENTS | MS-NOTIFICATION | `payment.received`  | Confirma o pagamento de uma parcela.           |

-----

## 3\. Detalhamento das Rotas e Regras Chave

Todas as rotas externas passam pelo **API Gateway** (`http://localhost:8080`).

### 3.1 Rotas de Acesso Público e Autenticação (MS-AUTHUSER)

| Método | Endpoint             | Autorização | Resumo                                  |
| :--- |:---------------------|:------------|:----------------------------------------|
| **POST** | `/api/auth/register` | PÚBLICO     | Cria um novo usuário (`CLIENT`).        |
| **POST** | `/api/auth/login`    | PÚBLICO     | Autentica e retorna o **JWT**.          |
| **POST** | `/api/users`         | ADMIN  | Retornar todos os usuários cadastrados. |

### 3.2 Rotas de Empréstimos (MS-LOAN)

| Método | Endpoint | Autorização | Resumo |
| :--- | :--- | :--- | :--- |
| **POST** | `/api/loans/simulate` | CLIENT | Simula empréstimo (cálculo Price). |
| **POST** | `/api/loans/request` | CLIENT | Solicita o empréstimo (dispara análise de crédito). |
| **GET** | `/api/loans` | CLIENT/ADMIN | Lista empréstimos. **A filtragem por usuário é garantida pelo MS-LOAN via JWT.** |
| **GET** | `/api/loans/{id}` | CLIENT/ADMIN | Busca um empréstimo específico por ID. |

### 3.3 Rotas de Pagamentos (MS-PAYMENTS)

| Método | Endpoint | Autorização | Resumo |
| :--- | :--- | :--- | :--- |
| **PUT** | `/api/installments/{id}/pay` | CLIENT | Processa o pagamento de uma parcela (aplica juros de mora se atrasado). |
| **GET** | `/api/installments/loan/{loanId}` | CLIENT/ADMIN | Lista todas as parcelas de um contrato. |

-----

## 4\. Infraestrutura e Setup Local

### 4.1 Requisitos de Segurança e Ambiente

Crie um arquivo chamado **`.env`** na raiz do projeto com a chave pública do JWT (essencial para o API Gateway validar o token):

```
JWT_PUBLIC_KEY="MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAwp/apITTXIbjYmSIXHjoXf9pdfzxw6zs4ZMu1P3Goi7RNM01XzKbhiLtNsXCTuj3vMeNSMQK9I1j1ai+Je0thKzT4NHBKV/aerK9v2Wz61VTJzNM0o6XW2DysalBLeYyZzIYCIMJ+CUKObyl2VVLMX5ysXfzpRJdJK/uV/6IouTBWqtN3Zq1Kf0cA+q/NERl9N2J23051wDcDIuoPDB7Y3T1f+evcrAkygVcrq31WNDF/hIwlygjGr73kHIFEPd0HjJwDbUjb1pocv3nSy7NKrRPEmr1peTxcFTqLoaGCIrjDCRaPhvJuSzB+Hk6wKwS/C5PX21VsrIDUvZ9JCa89QIDAQAB"
```

### 4.2 Guia de Execução

1.  **Pré-requisitos:** Docker, Docker Compose e o arquivo `.env`.
2.  **Permissão:** Garanta permissão de execução para os scripts de inicialização do banco de dados:
    ```bash
    chmod +x ./data/postgres-init/*.sh
    ```
3.  **Build e Start:** Navegue até a raiz do projeto e inicie o *stack*. O PostgreSQL usará a imagem **`postgres:17-alpine3.19`** otimizada.
    ```bash
    docker-compose up -d --build
    ```
4.  **Parar o Projeto:** Para desligar tudo:
    ```bash
    docker-compose down
    ```

### 4.3 Monitoramento e Acesso

| Serviço | Endpoint | Detalhe |
| :--- | :--- | :--- |
| **API Gateway** | `http://localhost:8080` | Entrada Principal |
| **MailHog (E-mails)** | `http://localhost:8025` | **Verificação de Notificações:** Todos os e-mails enviados pelo **MS-NOTIFICATION** são interceptados aqui. |
| **RabbitMQ Admin** | `http://localhost:15672` | Monitoramento da mensageria (user/pass: admin/admin) |
| **Grafana** | `http://localhost:3000` | **Observabilidade:** Dashboards com métricas do Prometheus e logs centralizados do Loki (user/pass: admin/gadmin) |

-----

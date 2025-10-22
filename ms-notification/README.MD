# README - Exportação de Mensagens de Empréstimo

Este documento descreve todos os eventos do sistema de notificações de empréstimos e como exportá-los como mensagens JSON para mensageria (RabbitMQ ou outro broker).

---

## Estrutura dos Eventos

### 1. Usuário Criado (UserDTO)
`{
  "eventId": "a5b61b78-2d29-4a14-8f6c-2f6e9db2d9c5",
  "timestamp": "2025-10-22T12:25:00",
  "userId": "a2d4cb14-15b8-42d1-8f58-d7f6312f8c5e",
  "name": "Tatiana Morais",
  "email": "tatiana.morais@email.com"
}`

### 2. Empréstimo Aprovado (ApprovedLoanDTO)
`{
  "eventId": "8a0b5a3e-5f3d-4c39-9f13-8c3db0f83b42",
  "timestamp": "2025-10-22T12:30:45",
  "loanId": "d3b6f0a4-4d9f-49a7-bd3a-38e1e36a84ac",
  "user": {
    "eventId": "a5b61b78-2d29-4a14-8f6c-2f6e9db2d9c5",
    "timestamp": "2025-10-22T12:25:00",
    "userId": "a2d4cb14-15b8-42d1-8f58-d7f6312f8c5e",
    "name": "Tatiana Morais",
    "email": "tatiana.morais@email.com"
  },
  "monthlyInstallmentValue": 1500.75,
  "termInMonths": 24,
  "firstDueDate": "2025-11-10",
  "totalAmount": 50000.00
}`

### 3. Empréstimo Rejeitado (DeniedLoanDTO)
`{
  "eventId": "b4e9c98f-7b6c-4b52-9c1a-0f4a7b2e6d8f",
  "timestamp": "2025-10-22T12:45:30",
  "loanId": "c3d6a0e5-1a5f-4b89-92ef-8f3a34b57a12",
  "user": {
    "eventId": "a5b61b78-2d29-4a14-8f6c-2f6e9db2d9c5",
    "timestamp": "2025-10-22T12:25:00",
    "userId": "a2d4cb14-15b8-42d1-8f58-d7f6312f8c5e",
    "name": "Tatiana Morais",
    "email": "tatiana.morais@email.com"
  },
  "rejectionReason": "Exceeded 50% monthly income limit"
}`

### 4. Pagamento Recebido (PaymentReceivedDTO)
`{
  "eventId": "f2d5f4a9-86f7-4c4d-9a2a-1a6a3a3dcd52",
  "timestamp": "2025-10-22T13:15:00",
  "installmentId": "d7a0b9e4-3c2f-4db8-8b61-1b5b2d7d4567",
  "loanId": "b6d4a5e7-24d8-4c9b-8f6b-9fcd93b4d234",
  "installmentNumber": 5,
  "valuePaid": 1520.50,
  "paymentDate": "2025-10-20",
  "user": {
    "eventId": "a5b61b78-2d29-4a14-8f6c-2f6e9db2d9c5",
    "timestamp": "2025-10-22T12:25:00",
    "userId": "a2d4cb14-15b8-42d1-8f58-d7f6312f8c5e",
    "name": "Tatiana Morais",
    "email": "tatiana.morais@email.com"
  }
}`

### 5. Empréstimo Finalizado (LoanCompletedDTO)
`{
  "eventId": "8a0b5a3e-5f3d-4c39-9f13-8c3db0f83b42",
  "timestamp": "2025-10-22T12:30:45",
  "loanId": "d3b6f0a4-4d9f-49a7-bd3a-38e1e36a84ac",
  "user": {
    "eventId": "a5b61b78-2d29-4a14-8f6c-2f6e9db2d9c5",
    "timestamp": "2025-10-22T12:25:00",
    "userId": "a2d4cb14-15b8-42d1-8f58-d7f6312f8c5e",
    "name": "Tatiana Morais",
    "email": "tatiana.morais@email.com"
  },
  "finalizationDate": "2025-10-22"
}
`

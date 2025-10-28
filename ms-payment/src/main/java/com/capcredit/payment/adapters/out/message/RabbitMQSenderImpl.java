package com.capcredit.payment.adapters.out.message;

import com.capcredit.payment.port.out.RabbitMqSender;
import com.capcredit.payment.port.out.dto.InstallmentDTO;
import com.capcredit.payment.port.out.dto.LoanCompletedDTO;
import com.capcredit.payment.port.out.dto.PaymentReceivedDTO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQSenderImpl implements RabbitMqSender {

    private static final Logger log = LoggerFactory.getLogger(RabbitMQSenderImpl.class);

    private final RabbitTemplate rabbitTemplate;
    private final String paymentQueue;
    private final String loanCompletedQueue;

    public RabbitMQSenderImpl(
            @Value("${broker.queue.received.payment}") String paymentQueue,
            @Value("${broker.queue.completed.payment}") String loanCompletedQueue,
            RabbitTemplate rabbitTemplate,
            MessageConverter jsonMessageConverter
    ) {
        this.paymentQueue = paymentQueue;
        this.loanCompletedQueue = loanCompletedQueue;
        this.rabbitTemplate = rabbitTemplate;
        this.rabbitTemplate.setMessageConverter(jsonMessageConverter);
        log.info("RabbitMQSenderImpl inicializado. Fila configurada: {}", paymentQueue);
    }

    @Override
    public void sendPaymentEvent(PaymentReceivedDTO paymentReceived) {
        log.info("Enviando evento de pagamento para a fila RabbitMQ...");
        log.debug("Detalhes do pagamento -> {}", paymentReceived);

        try {
            rabbitTemplate.convertAndSend(paymentQueue, paymentReceived);
            log.info("Evento de pagamento enviado com sucesso para a fila: {}", paymentQueue);
        } catch (Exception e) {
            log.error("Falha ao enviar evento de pagamento para a fila {}. Erro: {}", paymentQueue, e.getMessage(), e);
            throw new RuntimeException("Failed to send payment event to RabbitMQ", e);
        }
    }

    @Override
    public void sendLoanCompletedEvent(LoanCompletedDTO loanCompleted) {
        log.info("Sending loan completed event for loan {}...", loanCompleted.getLoanId());
        log.debug("Loan Completed Details -> {}", loanCompleted);

        try {
            rabbitTemplate.convertAndSend(loanCompletedQueue, loanCompleted);
            log.info("Completed loan event published successfully");
        } catch (Exception e) {
            log.error("Error on publish loan completed event {}.", e);
            throw new RuntimeException("Failed to send loan completed event to RabbitMQ", e);
        }
    }
}

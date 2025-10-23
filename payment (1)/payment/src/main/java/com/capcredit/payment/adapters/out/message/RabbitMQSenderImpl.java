package com.capcredit.payment.adapters.out.message;

import com.capcredit.payment.port.out.RabbitMqSender;
import com.capcredit.payment.port.out.dto.InstallmentDTO;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQSenderImpl implements RabbitMqSender {
    private final RabbitTemplate rabbitTemplate;
    private String paymentQueue;

    public RabbitMQSenderImpl(
            @Value("${broker.queue.payment.received}") String paymentQueue,
            RabbitTemplate rabbitTemplate,
            MessageConverter jsonMessageConverter
    ) {
        this.paymentQueue = paymentQueue;
        this.rabbitTemplate = rabbitTemplate;
        this.rabbitTemplate.setMessageConverter(jsonMessageConverter);
    }

    @Override
    public void sendPaymentEvent(InstallmentDTO installmentDTO) {
        try {
            rabbitTemplate.convertAndSend(paymentQueue, installmentDTO);
        } catch (Exception e) {
            throw new RuntimeException("Failed to send payment event to RabbitMQ", e);
        }
    }
}

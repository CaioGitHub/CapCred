package com.capcredit.ms_notification.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@Configuration
public class RabbitMQConfig {

    @Value("${broker.queue.created.user}")
    private String createdUser;

    @Bean
    public Queue queueCreatedUser() {
        return new Queue(createdUser, true);
    }

    @Value("${broker.queue.approved.loan}")
    private String approvedLoan;

    @Bean
    public Queue queueApprovedLoan() {
        return new Queue(approvedLoan, true);
    }

    @Value("${broker.queue.denied.loan}")
    private String deniedLoan;

    @Bean
    public Queue queueDeniedLoan() {
        return new Queue(deniedLoan, true);
    }

    @Value("${broker.queue.received.payment}")
    private String receivedPayment;

    @Bean
    public Queue queueReceivedPayment() {
        return new Queue(receivedPayment, true);
    }

    @Value("${broker.queue.completed.payment}")
    private String completedPayment;

    @Bean
    public Queue queueCompletedPayment() {
        return new Queue(completedPayment, true);
    }

    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        return new Jackson2JsonMessageConverter(objectMapper);
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(jackson2JsonMessageConverter());
        return template;
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory, Jackson2JsonMessageConverter converter) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(converter);
        return factory;
    }
}
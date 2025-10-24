package com.capcredit.ms_loan.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@Configuration
public class RabbitConfig {

    @Value("${broker.queue.loan.created}")
    private String loanCreatedQueue;

    @Value("${broker.queue.loan.approved}")
    private String loanApprovedQueue;

    @Value("${broker.queue.loan.rejected}")
    private String loanRejectedQueue;

    @Bean
    public Queue loanCreatedQueue() {
        return new Queue(loanCreatedQueue, true);
    }

    @Bean
    public Queue loanApprovedQueue() {
        return new Queue(loanApprovedQueue, true);
    }

    @Bean
    public Queue loanRejectedQueue() {
        return new Queue(loanRejectedQueue, true);
    }

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
}

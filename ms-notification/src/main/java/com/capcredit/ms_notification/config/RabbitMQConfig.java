package com.capcredit.ms_notification.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
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

    @Value("${broker.exchange.loan}")
    private String loanExchangeName;

    @Value("${broker.queue.approved.loan.notification}")
    private String approvedLoanNotificationQueue;

    @Bean
    public Queue queueCreatedUser() {
        return new Queue(createdUser, true);
    }

//    @Bean
//    public Queue queueApprovedLoan() {
//        return new Queue(approvedLoan, true);
//    }

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
    public TopicExchange loanTopicExchange() {
        return new TopicExchange(loanExchangeName, true, false);
    }

    @Bean
    public Queue queueApprovedLoanNotification() {
        return new Queue(approvedLoanNotificationQueue, true);
    }

    @Bean
    public Binding approvedLoanNotificationBinding(Queue queueApprovedLoanNotification, TopicExchange loanTopicExchange) {
        return BindingBuilder.bind(queueApprovedLoanNotification)
                .to(loanTopicExchange)
                .with("loan.approved");
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
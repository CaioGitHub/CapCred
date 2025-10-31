package com.capcredit.ms_loan.config;

import java.util.List;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import com.fasterxml.jackson.databind.DeserializationFeature;
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

    @Value("${broker.queue.loan.completed}")
    private String loanCompletedQueue;

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

    @Bean
    public Queue loanCompletedQueue() {
        return new Queue(loanCompletedQueue, true);
    }

    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return new Jackson2JsonMessageConverter(objectMapper);
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(jackson2JsonMessageConverter());
        template.setBeforePublishPostProcessors(propagateUserContextToMessage());
        return template;
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory, Jackson2JsonMessageConverter converter) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(converter);
        factory.setAfterReceivePostProcessors(setupSecurityContextFromMessage()); 
        return factory;
    }

    public MessagePostProcessor propagateUserContextToMessage() {
        return (Message message) -> {
            var authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null) return message;
            
            var properties = message.getMessageProperties();

            var userId = authentication.getName();
            properties.setHeader("X-User-ID", userId);

            var userRole = authentication.getAuthorities().stream().findFirst().get().toString();
            properties.setHeader("X-User-Role", userRole);

            return message;
        };
    }

    public MessagePostProcessor setupSecurityContextFromMessage() {
        return (Message message) -> {
            var properties = message.getMessageProperties();
            String userId = properties.getHeader("X-User-ID");
            String userRole = properties.getHeader("X-User-Role");

            if(userId != null && userRole != null) {
                List<SimpleGrantedAuthority> roles = List.of(new SimpleGrantedAuthority(userRole));
                var authentication = new UsernamePasswordAuthenticationToken(userId,  null, roles);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }

            return message;
        };
    }
}

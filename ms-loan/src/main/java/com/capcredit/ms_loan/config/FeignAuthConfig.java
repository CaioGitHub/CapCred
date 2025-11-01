package com.capcredit.ms_loan.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.context.SecurityContextHolder;

import feign.RequestInterceptor;
import feign.RequestTemplate;

@Configuration
public class FeignAuthConfig {
    
    @Bean
    public RequestInterceptor relayAuthorizationHeader() {
        return (RequestTemplate template) -> {
            var authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null) return;

            var userId = authentication.getName();
            template.header("X-User-ID", userId);

            var userRole = authentication.getAuthorities().stream().findFirst().get().toString();
            template.header("X-User-Role", userRole);
        };
    }
}

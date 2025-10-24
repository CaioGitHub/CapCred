package com.capcredit.ms_loan.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import feign.RequestInterceptor;
import feign.RequestTemplate;

@Configuration
public class FeignAuthConfig {
    
    @Bean
    public RequestInterceptor relayAuthorizationHeader() {
        return (RequestTemplate template) -> {
            var attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attrs == null) return;
            
            var request = attrs.getRequest();

            template.header("X-User-ID", request.getHeader("X-User-ID"));
            template.header("X-User-Role", request.getHeader("X-User-Role"));
        };
    }
}

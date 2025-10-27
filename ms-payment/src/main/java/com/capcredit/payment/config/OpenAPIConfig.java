package com.capcredit.payment.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
    info = @Info(
        title = "CapCredit - MS Payment",
        version = "1.0",
        description = "API responsible for payment processing."
    )
)
public class OpenAPIConfig {}

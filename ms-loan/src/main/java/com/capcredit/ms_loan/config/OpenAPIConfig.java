package com.capcredit.ms_loan.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
    info = @Info(
        title = "CapCredit - MS Loan",
        version = "1.0",
        description = "API responsible for loan calculation, application, tracking."
    )
)
public class OpenAPIConfig {}

package com.brandshield.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI brandShieldOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("BrandShield AI API")
                .description("AI-Powered Digital Risk Protection Platform - REST API Documentation")
                .version("1.0.0")
                .contact(new Contact()
                    .name("BrandShield AI Team")));
    }
}

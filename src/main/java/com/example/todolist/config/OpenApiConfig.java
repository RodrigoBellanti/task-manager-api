package com.example.todolist.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI taskManagerOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Task Manager API")
                        .description("REST API for managing tasks with pagination, filtering, and validation")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Rodrigo")
                                .email("rodrigobellanti@gmail.com")
                                .url("https://github.com/rodrigobellanti/task-manager-api")));
    }
}
package com.codegym.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Configuration class for OpenAPI/Swagger documentation.
 * 
 * This class configures the API documentation with proper security schemes,
 * server information, and metadata about the Rental House API.
 * 
 * @author CodeGym Team
 * @version 1.0
 * @since 2024
 */
@Configuration
public class OpenApiConfig {

    @Value("${server.port:8080}")
    private String serverPort;

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Rental House API")
                        .description("""
                                A comprehensive REST API for managing rental house operations.
                                
                                ## Features
                                - User authentication and authorization
                                - House listing and management
                                - Rental booking system
                                - Review and rating system
                                - Notification system
                                - Admin dashboard
                                
                                ## Authentication
                                This API uses JWT (JSON Web Token) for authentication.
                                Include the token in the Authorization header: `Bearer <your-token>`
                                
                                ## Roles
                                - **USER**: Regular users who can rent houses and leave reviews
                                - **HOST**: Property owners who can manage their listings
                                - **ADMIN**: System administrators with full access
                                """)
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("CodeGym Team")
                                .email("support@codegym.com")
                                .url("https://codegym.com"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:" + serverPort)
                                .description("Development server"),
                        new Server()
                                .url("https://api.rentalhouse.com")
                                .description("Production server")))
                .addSecurityItem(new SecurityRequirement().addList("Bearer Authentication"))
                .components(new Components()
                        .addSecuritySchemes("Bearer Authentication", createAPIKeyScheme()));
    }

    private SecurityScheme createAPIKeyScheme() {
        return new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .bearerFormat("JWT")
                .scheme("bearer")
                .description("Enter JWT token in the format: Bearer <token>");
    }
}


package com.jobtrackingapp.software_service.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        final String securitySchemeName = "BearerAuth";

        return new OpenAPI()
                .info(new Info()
                        .title("SOFTWARE SERVICE")
                        .version("1.0.0")
                        .description("Software Servisine ait apiler"))
                .servers(List.of(
                        new Server().url("http://localhost:8500").description("API Gateway")
                ))
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName)) // Authorization başlığı
                .components(new io.swagger.v3.oas.models.Components()
                        .addSecuritySchemes(securitySchemeName, new SecurityScheme()
                                .name("Authorization") // Başlık adı
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT"))); // Token formatı
    }
}

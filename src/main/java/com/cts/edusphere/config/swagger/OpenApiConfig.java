package com.cts.edusphere.config.swagger;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

/**
 * SpringDoc / Swagger UI configuration for the EduSphere REST API.
 *
 * <p>Registers a custom {@link OpenAPI} bean that adds a global JWT Bearer
 * authentication scheme so that every endpoint in the Swagger UI can be
 * tested with a valid access token.</p>
 */
@Configuration
public class OpenApiConfig {

    /**
     * Produces the {@link OpenAPI} descriptor used by SpringDoc to generate the
     * API documentation and the Swagger UI.
     *
     * <p>The descriptor configures:</p>
     * <ul>
     *   <li>API title and version metadata</li>
     *   <li>A global security requirement named {@code bearerAuth}</li>
     *   <li>A corresponding {@code HTTP Bearer / JWT} security scheme component</li>
     * </ul>
     *
     * @return the fully configured {@link OpenAPI} instance
     */
    @Bean
    public OpenAPI customOpenAPI() {
        String bearerAuth = "bearerAuth";

        return new OpenAPI()
            .info(new Info().title("Edusphere API").version("1.0"))
            .addSecurityItem(new SecurityRequirement().addList(bearerAuth))
            .components(new Components()
                .addSecuritySchemes(bearerAuth, new SecurityScheme()
                    .name(bearerAuth)
                    .type(SecurityScheme.Type.HTTP)
                    .scheme("bearer")
                    .bearerFormat("JWT")));
    }
}
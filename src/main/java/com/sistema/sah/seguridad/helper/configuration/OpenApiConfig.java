package com.sistema.sah.seguridad.helper.configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

/**
 * Configuración de OpenAPI para la documentación de la API.
 * <p>
 * Define la información principal de la API, el esquema de seguridad
 * y los requisitos de seguridad para los endpoints protegidos.
 * </p>
 */
@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Documentación de la API de Sistema SAH",
                version = "v1.0",
                description = "Esta API gestiona los recursos del sistema SAH, incluyendo autenticación, usuarios y servicios.",
                contact = @Contact(
                        name = "Equipo de Soporte",
                        email = "soporte@sistema-sah.com",
                        url = "https://www.sistema-sah.com/soporte"
                ),
                license = @License(
                        name = "Licencia MIT",
                        url = "https://opensource.org/licenses/MIT"
                )
        ),
        security = {@SecurityRequirement(name = "bearer-key")}
)
@SecurityScheme(
        name = "bearer-key",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT"
)
public class OpenApiConfig {
    // Clase de configuración para OpenAPI. No contiene lógica adicional.
}

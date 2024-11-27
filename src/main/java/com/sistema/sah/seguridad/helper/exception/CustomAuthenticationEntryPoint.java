package com.sistema.sah.seguridad.helper.exception;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Manejador personalizado para errores de autenticación.
 * <p>
 * Este manejador se activa cuando un usuario no autenticado intenta acceder a un recurso protegido
 * y devuelve una respuesta personalizada con un código de estado 401.
 * </p>
 */
@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private static final String JSON_CONTENT_TYPE = "application/json";

    /**
     * Maneja intentos de acceso no autenticados devolviendo un mensaje personalizado.
     *
     * @param request       la solicitud HTTP que causó el error de autenticación.
     * @param response      la respuesta HTTP que se enviará al cliente.
     * @param authException la excepción que describe el motivo del error de autenticación.
     * @throws IOException      si ocurre un error de entrada/salida al escribir la respuesta.
     * @throws ServletException si ocurre un error en el manejo del servlet.
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException, ServletException {

        // Configurar el tipo de contenido y el estado HTTP
        response.setContentType(JSON_CONTENT_TYPE);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        // Registrar un mensaje de error en los logs
        logError(request, authException);

        // Escribir el mensaje de error en formato JSON
        String errorMessage = String.format("{\"error\": \"Autenticación fallida: %s\"}", authException.getMessage());
        response.getWriter().write(errorMessage);
    }

    /**
     * Registra detalles sobre el error de autenticación para fines de auditoría o depuración.
     *
     * @param request       la solicitud que causó el error.
     * @param authException la excepción que describe el motivo del error de autenticación.
     */
    private void logError(HttpServletRequest request, AuthenticationException authException) {
        String requestURI = request.getRequestURI();
        String method = request.getMethod();
        System.err.printf("Error de autenticación en %s %s: %s%n", method, requestURI, authException.getMessage());
    }
}

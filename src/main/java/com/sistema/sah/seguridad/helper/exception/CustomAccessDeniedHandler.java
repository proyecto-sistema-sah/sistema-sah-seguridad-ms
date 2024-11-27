package com.sistema.sah.seguridad.helper.exception;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Manejador personalizado para accesos denegados.
 * <p>
 * Este manejador se activa cuando un usuario intenta acceder a un recurso
 * al que no tiene permisos y devuelve una respuesta personalizada con un código de estado 403.
 * </p>
 */
@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private static final String JSON_CONTENT_TYPE = "application/json";
    private static final String ERROR_MESSAGE = "{\"error\": \"Acceso denegado: No tienes permiso para acceder a este recurso.\"}";

    /**
     * Maneja accesos denegados devolviendo una respuesta con un mensaje personalizado.
     *
     * @param request               la solicitud HTTP que causó el acceso denegado.
     * @param response              la respuesta HTTP que se enviará al cliente.
     * @param accessDeniedException la excepción que describe el motivo del acceso denegado.
     * @throws IOException      si ocurre un error de entrada/salida al escribir la respuesta.
     * @throws ServletException si ocurre un error en el manejo del servlet.
     */
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException)
            throws IOException, ServletException {

        // Configurar el tipo de contenido y el estado HTTP
        response.setContentType(JSON_CONTENT_TYPE);
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);

        // Registrar un mensaje de error en los logs
        logError(request, accessDeniedException);

        // Escribir el mensaje de error en la respuesta
        response.getWriter().write(ERROR_MESSAGE);
    }

    /**
     * Registra detalles sobre el acceso denegado para fines de auditoría o depuración.
     *
     * @param request               la solicitud que causó el acceso denegado.
     * @param accessDeniedException la excepción que describe el motivo del acceso denegado.
     */
    private void logError(HttpServletRequest request, AccessDeniedException accessDeniedException) {
        String requestURI = request.getRequestURI();
        String method = request.getMethod();
        System.err.printf("Acceso denegado en %s %s: %s%n", method, requestURI, accessDeniedException.getMessage());
    }
}

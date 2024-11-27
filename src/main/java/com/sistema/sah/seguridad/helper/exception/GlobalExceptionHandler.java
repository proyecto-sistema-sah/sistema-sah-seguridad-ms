package com.sistema.sah.seguridad.helper.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Manejador global de excepciones para la aplicación.
 * <p>
 * Este manejador captura excepciones específicas y genéricas para devolver
 * respuestas estructuradas y consistentes a los clientes.
 * </p>
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Maneja excepciones genéricas no controladas (500 Internal Server Error).
     *
     * @param ex      la excepción capturada.
     * @param request información de la solicitud donde ocurrió la excepción.
     * @return una respuesta estructurada con detalles del error.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleAllExceptions(Exception ex, WebRequest request) {
        Map<String, Object> body = buildErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Internal Server Error",
                ex.getMessage(),
                request
        );
        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Maneja errores relacionados con recursos no encontrados (404 Not Found).
     *
     * @param ex      la excepción capturada.
     * @param request información de la solicitud donde ocurrió la excepción.
     * @return una respuesta estructurada con detalles del error.
     */
    @ExceptionHandler(HttpClientErrorException.NotFound.class)
    public ResponseEntity<Object> handleResourceNotFoundException(HttpClientErrorException.NotFound ex, WebRequest request) {
        Map<String, Object> body = buildErrorResponse(
                HttpStatus.NOT_FOUND,
                "Resource Not Found",
                ex.getMessage(),
                request
        );
        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    /**
     * Maneja excepciones de validación de argumentos de método (400 Bad Request).
     *
     * @param ex la excepción capturada.
     * @return una respuesta estructurada con detalles de los errores de validación.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> validationErrors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            validationErrors.put(fieldName, errorMessage);
        });

        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("error", "Validation Error");
        body.put("message", "Validation failed for one or more fields");
        body.put("validationErrors", validationErrors);

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    /**
     * Construye una respuesta de error estructurada.
     *
     * @param status     el estado HTTP asociado con la excepción.
     * @param error      el tipo de error.
     * @param message    el mensaje descriptivo del error.
     * @param request    la solicitud donde ocurrió el error.
     * @return un mapa con los detalles del error.
     */
    private Map<String, Object> buildErrorResponse(HttpStatus status, String error, String message, WebRequest request) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", status.value());
        body.put("error", error);
        body.put("message", message);
        body.put("path", request.getDescription(false).replace("uri=", ""));
        return body;
    }
}

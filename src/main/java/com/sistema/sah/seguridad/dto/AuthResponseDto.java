package com.sistema.sah.seguridad.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO que representa la respuesta de autenticación.
 * <p>
 * Contiene la información del token generado después de un inicio de sesión exitoso
 * o una operación relacionada con la autenticación.
 * </p>
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponseDto {

    /**
     * Token JWT generado para el usuario autenticado.
     */
    private String token;
}

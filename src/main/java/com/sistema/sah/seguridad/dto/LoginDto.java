package com.sistema.sah.seguridad.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO que representa las credenciales de inicio de sesión.
 * <p>
 * Contiene la información necesaria para autenticar a un usuario,
 * como el correo electrónico y la contraseña.
 * </p>
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginDto {

    /**
     * Correo electrónico del usuario que desea iniciar sesión.
     * <p>
     * Este campo es obligatorio y se utiliza como identificador del usuario.
     * </p>
     */
    private String email;

    /**
     * Contraseña del usuario.
     * <p>
     * Este campo es obligatorio y debe ser válido para autenticar al usuario.
     * </p>
     */
    private String contrasena;

}

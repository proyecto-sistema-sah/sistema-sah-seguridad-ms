package com.sistema.sah.seguridad.service;

import com.sistema.sah.commons.dto.UsuarioDto;
import com.sistema.sah.seguridad.dto.AuthResponseDto;
import com.sistema.sah.seguridad.dto.UserSecurityDto;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * Servicio para gestionar la generación, validación y extracción de información de tokens JWT.
 */
public interface IJwtService {

    /**
     * Genera un token JWT basado en los datos del usuario y los detalles de seguridad.
     *
     * @param usuarioData los datos del usuario contenidos en un {@link UsuarioDto}.
     * @param usuarioDto  los detalles de seguridad del usuario contenidos en un {@link UserDetails}.
     * @return un token JWT como {@link String}.
     */
    String getToken(UsuarioDto usuarioData, UserDetails usuarioDto);

    /**
     * Extrae el nombre de usuario (username) de un token JWT.
     *
     * @param token el token JWT del cual se extraerá el nombre de usuario.
     * @return el nombre de usuario como {@link String}.
     */
    String getUsernameFromToken(String token);

    /**
     * Genera un token JWT basado en los datos de seguridad del usuario.
     *
     * @param usuarioDto los datos del usuario contenidos en un {@link UserSecurityDto}.
     * @return una respuesta de autenticación que incluye el token, encapsulada en un {@link AuthResponseDto}.
     */
    AuthResponseDto generarToken(UserSecurityDto usuarioDto);

    /**
     * Verifica si un token JWT es válido para un usuario específico.
     *
     * @param token       el token JWT a validar.
     * @param userDetails los detalles del usuario contenidos en un {@link UserDetails}.
     * @return {@code true} si el token es válido, {@code false} en caso contrario.
     */
    boolean isTokenValid(String token, UserDetails userDetails);
}

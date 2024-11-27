package com.sistema.sah.seguridad.service;

/**
 * Servicio para gestionar la lista negra de tokens JWT.
 * <p>
 * Proporciona métodos para añadir tokens a la lista negra y verificar si un token específico está en ella.
 * </p>
 */
public interface ITokenBlackListService {

    /**
     * Añade un token a la lista negra.
     * <p>
     * Este método debe ser utilizado cuando se desee invalidar un token JWT,
     * como en casos de cierre de sesión o compromisos de seguridad.
     * </p>
     *
     * @param token el token JWT que se añadirá a la lista negra.
     */
    void blackListToken(String token);

    /**
     * Verifica si un token está en la lista negra.
     * <p>
     * Permite validar si un token JWT específico ha sido invalidado previamente.
     * </p>
     *
     * @param token el token JWT a verificar.
     * @return {@code true} si el token está en la lista negra, {@code false} en caso contrario.
     */
    boolean isTokenBlackListed(String token);
}

package com.sistema.sah.seguridad.repository;

import com.sistema.sah.commons.entity.BlackListTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio para gestionar la lista negra de tokens.
 * <p>
 * Proporciona métodos para interactuar con la entidad {@link BlackListTokenEntity},
 * incluyendo verificaciones de existencia de tokens en la lista negra.
 * </p>
 */
@Repository
public interface BlackListTokenRepository extends JpaRepository<BlackListTokenEntity, Integer> {

    /**
     * Verifica si un token existe en la lista negra.
     *
     * @param token el token a verificar.
     * @return {@code true} si el token está en la lista negra, {@code false} de lo contrario.
     */
    Boolean existsByToken(String token);

}

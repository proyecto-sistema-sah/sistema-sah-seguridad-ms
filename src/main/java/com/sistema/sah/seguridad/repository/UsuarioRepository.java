package com.sistema.sah.seguridad.repository;

import com.sistema.sah.commons.entity.UsuarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositorio para gestionar las operaciones relacionadas con la entidad {@link UsuarioEntity}.
 * <p>
 * Proporciona métodos para interactuar con la base de datos, incluyendo
 * la búsqueda de usuarios por su correo electrónico.
 * </p>
 */
@Repository
public interface UsuarioRepository extends JpaRepository<UsuarioEntity, String> {

    /**
     * Busca un usuario por su correo electrónico.
     *
     * @param correoUsuario el correo electrónico del usuario a buscar.
     * @return un {@link Optional} que contiene la entidad {@link UsuarioEntity} si se encuentra, o vacío si no existe.
     */
    Optional<UsuarioEntity> findByCorreoUsuario(String correoUsuario);

}

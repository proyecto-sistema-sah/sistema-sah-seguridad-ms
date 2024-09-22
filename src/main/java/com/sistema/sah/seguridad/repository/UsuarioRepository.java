package com.sistema.sah.seguridad.repository;

import com.sistema.sah.commons.entity.UsuarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<UsuarioEntity, String> {

    Optional<UsuarioEntity> findByCorreoUsuario(String correoUsuario);

}

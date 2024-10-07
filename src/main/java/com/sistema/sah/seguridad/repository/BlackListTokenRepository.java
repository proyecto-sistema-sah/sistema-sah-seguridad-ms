package com.sistema.sah.seguridad.repository;

import com.sistema.sah.commons.entity.BlackListTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BlackListTokenRepository extends JpaRepository<BlackListTokenEntity, Integer> {

    Boolean existsByToken(String token);

}

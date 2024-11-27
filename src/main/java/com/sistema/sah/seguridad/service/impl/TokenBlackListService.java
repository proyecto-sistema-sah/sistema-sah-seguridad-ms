package com.sistema.sah.seguridad.service.impl;

import com.sistema.sah.commons.dto.BlackListTokenDto;
import com.sistema.sah.commons.dto.UsuarioDto;
import com.sistema.sah.commons.helper.mapper.IBlackListTokenMapper;
import com.sistema.sah.commons.helper.mapper.IBlackListTokenMapperImpl;
import com.sistema.sah.commons.helper.util.Utilidades;
import com.sistema.sah.seguridad.repository.BlackListTokenRepository;
import com.sistema.sah.seguridad.service.ITokenBlackListService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * Implementación del servicio para gestionar la lista negra de tokens JWT.
 * <p>
 * Permite añadir tokens a la lista negra e identificar si un token específico
 * está en ella.
 * </p>
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class TokenBlackListService implements ITokenBlackListService {

    private final BlackListTokenRepository blackListTokenRepository;
    private final IBlackListTokenMapperImpl blackListTokenMapper;
    private final JwtService jwtService;

    /**
     * Añade un token a la lista negra.
     * <p>
     * Extrae la fecha de expiración y el código de usuario del token para
     * registrar la información en la lista negra.
     * </p>
     *
     * @param token el token JWT que se añadirá a la lista negra.
     */
    @Override
    @Transactional
    public void blackListToken(String token) {
        try {
            Date expirationDate = jwtService.getExpiration(token);
            String codigoUsuario = jwtService.getCodigoUsuario(token);

            BlackListTokenDto blackListTokenDto = BlackListTokenDto.builder()
                    .token(token)
                    .fechaExpiracion(Utilidades.convertToLocalDateTime(expirationDate))
                    .codigoUsuarioDtoFk(
                            UsuarioDto.builder()
                                    .codigoUsuario(codigoUsuario)
                                    .build()
                    )
                    .build();

            blackListTokenRepository.save(blackListTokenMapper.dtoToEntity(blackListTokenDto));

            log.info("Token añadido a la lista negra: {}", token);
        } catch (Exception e) {
            log.error("Error al añadir el token a la lista negra: {}", token, e);
            throw new RuntimeException("No se pudo añadir el token a la lista negra.");
        }
    }

    /**
     * Verifica si un token está en la lista negra.
     *
     * @param token el token JWT a verificar.
     * @return {@code true} si el token está en la lista negra, {@code false} en caso contrario.
     */
    @Override
    public boolean isTokenBlackListed(String token) {
        try {
            boolean isBlackListed = blackListTokenRepository.existsByToken(token);
            log.info("Verificación del token en la lista negra: {}, Resultado: {}", token, isBlackListed);
            return isBlackListed;
        } catch (Exception e) {
            log.error("Error al verificar el token en la lista negra: {}", token, e);
            throw new RuntimeException("No se pudo verificar el token en la lista negra.");
        }
    }
}

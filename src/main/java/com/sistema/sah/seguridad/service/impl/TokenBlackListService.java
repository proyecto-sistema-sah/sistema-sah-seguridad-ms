package com.sistema.sah.seguridad.service.impl;

import com.sistema.sah.commons.dto.BlackListTokenDto;
import com.sistema.sah.commons.dto.UsuarioDto;
import com.sistema.sah.commons.helper.mapper.IBlackListTokenMapper;
import com.sistema.sah.commons.helper.mapper.IBlackListTokenMapperImpl;
import com.sistema.sah.commons.helper.util.Utilidades;
import com.sistema.sah.seguridad.repository.BlackListTokenRepository;
import com.sistema.sah.seguridad.service.ITokenBlackListService;
import io.jsonwebtoken.Jwt;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class TokenBlackListService implements ITokenBlackListService {

    private final BlackListTokenRepository blackListTokenRepository;

    private final IBlackListTokenMapperImpl iBlackListTokenMapper;

    private final JwtService jwtService;

    @Override
    @Transactional
    public void blackListToken(String token) {
        Date tiempo = jwtService.getExpiration(token);
        String codigoUsuario = jwtService.getCodigoUsuario(token);
        BlackListTokenDto  blackListTokenDto = BlackListTokenDto.builder()
                .token(token)
                .fechaExpiracion(Utilidades.convertToLocalDateTime(tiempo))
                        .codigoUsuarioDtoFk(UsuarioDto.builder().codigoUsuario(codigoUsuario).build()).build();
        blackListTokenRepository.save(iBlackListTokenMapper.dtoToEntity(blackListTokenDto));
    }

    @Override
    public boolean isTokenBlackListed(String token) {
        return blackListTokenRepository.existsByToken(token);
    }
}

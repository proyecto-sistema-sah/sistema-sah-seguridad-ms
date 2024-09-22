package com.sistema.sah.seguridad.service.impl;

import com.sistema.sah.commons.dto.RespuestaGeneralDto;
import com.sistema.sah.commons.entity.UsuarioEntity;
import com.sistema.sah.commons.helper.mapper.UsuarioMapper;
import com.sistema.sah.seguridad.dto.AuthResponseDto;
import com.sistema.sah.seguridad.dto.LoginDto;
import com.sistema.sah.seguridad.dto.UserSecurityDto;
import com.sistema.sah.seguridad.repository.UsuarioRepository;
import com.sistema.sah.seguridad.service.IQueryUsuarioService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Log4j2
public class QueryUsuarioServiceImpl implements IQueryUsuarioService {

    private final UsuarioRepository usuarioRepository;

    private final UsuarioMapper usuarioMapper;

    private final AuthenticationManager authenticationManager;

    private final JwtService jwtService;

    @Override
    @Transactional(readOnly = true)
    public RespuestaGeneralDto findAllUsuario() {
        RespuestaGeneralDto respuesta = new RespuestaGeneralDto();
        respuesta.setData(usuarioMapper.listEntityTolistDto(usuarioRepository.findAll()));
        respuesta.setMessage("Se consultaron correctamente el listado de usuarios");
        respuesta.setStatus(HttpStatus.OK);
       return respuesta;
    }

    @Override
    public RespuestaGeneralDto loginUser(LoginDto loginDto) {
        RespuestaGeneralDto respuestaGeneralDto = new RespuestaGeneralDto();
        respuestaGeneralDto.setMessage("Se logio correctamente");
        respuestaGeneralDto.setStatus(HttpStatus.OK);
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getContrasena()));
        UsuarioEntity usuarioEntity = usuarioRepository.findByCorreoUsuario(loginDto.getEmail()).orElseThrow();
        UserDetails user = new UserSecurityDto(usuarioMapper.entityToDto(usuarioEntity));
        String token = jwtService.getToken(user);
        respuestaGeneralDto.setData(AuthResponseDto.builder().token(token).build());
        return respuestaGeneralDto;
    }
}

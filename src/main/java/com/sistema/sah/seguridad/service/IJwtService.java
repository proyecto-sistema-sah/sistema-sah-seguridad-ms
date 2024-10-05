package com.sistema.sah.seguridad.service;
import com.sistema.sah.commons.dto.UsuarioDto;
import org.springframework.security.core.userdetails.UserDetails;

public interface IJwtService {

    String getToken(UsuarioDto usuarioData, UserDetails usuarioDto);

    String getUsernameFromToken(String token);

    boolean isTokenValid(String token, UserDetails userDetails);
}

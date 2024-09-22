package com.sistema.sah.seguridad.service;
import org.springframework.security.core.userdetails.UserDetails;

public interface IJwtService {

    String getToken(UserDetails usuarioDto);

    String getUsernameFromToken(String token);

    boolean isTokenValid(String token, UserDetails userDetails);
}

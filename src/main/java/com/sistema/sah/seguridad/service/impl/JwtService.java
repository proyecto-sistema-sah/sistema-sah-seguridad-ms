package com.sistema.sah.seguridad.service.impl;

import com.sistema.sah.commons.dto.UsuarioDto;
import com.sistema.sah.seguridad.dto.AuthResponseDto;
import com.sistema.sah.seguridad.dto.UserSecurityDto;
import com.sistema.sah.seguridad.service.IJwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Servicio para gestionar operaciones relacionadas con tokens JWT.
 * <p>
 * Proporciona métodos para generar, validar y extraer información de los tokens JWT.
 * </p>
 */
@Service
@Log4j2
@RequiredArgsConstructor
public class JwtService implements IJwtService {

    @Value("${jwt.secret}")
    private String secretKey;

    private static final long TOKEN_EXPIRATION_MILLIS = 1000 * 60 * 60 * 24; // 24 horas

    /**
     * Genera un token JWT para un usuario con claims adicionales.
     *
     * @param usuarioData los datos del usuario.
     * @param userDetails los detalles de seguridad del usuario.
     * @return el token JWT generado.
     */
    @Override
    public String getToken(UsuarioDto usuarioData, UserDetails userDetails) {
        Map<String, Object> additionalClaims = buildAdditionalClaims(usuarioData);
        return buildToken(additionalClaims, userDetails);
    }

    /**
     * Extrae el nombre de usuario del token JWT.
     *
     * @param token el token JWT.
     * @return el nombre de usuario contenido en el token.
     */
    @Override
    public String getUsernameFromToken(String token) {
        return getClaims(token, Claims::getSubject);
    }

    /**
     * Valida si un token JWT es válido para un usuario específico.
     *
     * @param token       el token JWT.
     * @param userDetails los detalles del usuario.
     * @return {@code true} si el token es válido, {@code false} en caso contrario.
     */
    @Override
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    /**
     * Genera un token JWT encapsulado en un objeto {@link AuthResponseDto}.
     *
     * @param userSecurityDto los datos del usuario para la generación del token.
     * @return un objeto que contiene el token generado.
     */
    @Override
    public AuthResponseDto generarToken(UserSecurityDto userSecurityDto) {
        return AuthResponseDto.builder()
                .token(getToken(userSecurityDto, userSecurityDto))
                .build();
    }

    /**
     * Extrae el código del usuario desde un token JWT.
     *
     * @param token el token JWT.
     * @return el código del usuario contenido en el token.
     */
    public String getCodigoUsuario(String token) {
        return getClaims(token, claims -> claims.get("codigoUsuario", String.class));
    }

    /**
     * Extrae la fecha de expiración del token JWT.
     *
     * @param token el token JWT.
     * @return la fecha de expiración.
     */
    public Date getExpiration(String token) {
        return getClaims(token, Claims::getExpiration);
    }

    /**
     * Verifica si un token JWT ha expirado.
     *
     * @param token el token JWT.
     * @return {@code true} si el token ha expirado, {@code false} en caso contrario.
     */
    private boolean isTokenExpired(String token) {
        return getExpiration(token).before(new Date());
    }

    /**
     * Obtiene todos los claims de un token JWT.
     *
     * @param token el token JWT.
     * @return los claims del token.
     */
    private Claims getAllClaims(String token) {
        return Jwts.parser()
                .setSigningKey(Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey)))
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Construye un token JWT.
     *
     * @param extraClaims los claims adicionales a incluir en el token.
     * @param userDetails los detalles del usuario.
     * @return el token JWT generado.
     */
    private String buildToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + TOKEN_EXPIRATION_MILLIS))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Construye los claims adicionales para un token JWT basado en los datos del usuario.
     *
     * @param usuarioData los datos del usuario.
     * @return un mapa de claims adicionales.
     */
    private Map<String, Object> buildAdditionalClaims(UsuarioDto usuarioData) {
        Map<String, Object> additionalClaims = new HashMap<>();
        additionalClaims.put("codigoUsuario", usuarioData.getCodigoUsuario());
        additionalClaims.put("foto", usuarioData.getCodigoImagenUsuario());
        additionalClaims.put("rol", usuarioData.getTipoUsuarioDtoFk().getNombreTipoUsuario());
        additionalClaims.put("nombreCompleto", usuarioData.getNombresUsuario() + " " + usuarioData.getApellidosUsuario());
        return additionalClaims;
    }

    /**
     * Obtiene un valor específico de los claims del token utilizando un resolver.
     *
     * @param token          el token JWT.
     * @param claimsResolver la función para resolver el valor del claim.
     * @param <T>            el tipo del valor del claim.
     * @return el valor resuelto.
     */
    private <T> T getClaims(String token, Function<Claims, T> claimsResolver) {
        Claims claims = getAllClaims(token);
        return claimsResolver.apply(claims);
    }
}

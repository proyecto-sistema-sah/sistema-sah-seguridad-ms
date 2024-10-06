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

@Service
@Log4j2
@RequiredArgsConstructor
public class JwtService implements IJwtService {

    @Value("${jwt.secret}")
    private String secretKey;

    @Override
    public String getToken(UsuarioDto usuarioData, UserDetails usuarioDto) {
        Map<String, Object> additionalClaims = new HashMap<>();
        additionalClaims.put("codigoUsuario", usuarioData.getCodigoUsuario());
        additionalClaims.put("foto", usuarioData.getCodigoImagenUsuario());
        additionalClaims.put("rol", usuarioData.getTipoUsuarioDtoFk().getNombreTipoUsuario());
        additionalClaims.put("nombreCompleto", usuarioData.getNombresUsuario() + ' ' + usuarioData.getApellidosUsuario());
        return buildToken(additionalClaims, usuarioDto);
    }

    @Override
    public String getUsernameFromToken(String token) {
        return getClaims(token, Claims::getSubject);
    }

    @Override
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    public AuthResponseDto generarToken(UserSecurityDto usuarioDto){
        return AuthResponseDto.builder().token(getToken(usuarioDto, usuarioDto)).build();
    }

    public <T> T getClaims(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Date getExpiration(String token) {
        return getClaims(token, Claims::getExpiration);
    }

    private boolean isTokenExpired(String token) {
        return getExpiration(token).before(new Date());
    }

    private Claims getAllClaims(String token) {
        return Jwts.parser().verifyWith(Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey))).build().parseClaimsJws(token).getBody();
    }

    private String buildToken(Map<String, Object> extraClaims, UserDetails user){
        SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey)); // o HS384, HS512 dependiendo de lo que uses
        return Jwts.builder().claims(extraClaims)
                .subject(user.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis()+1000*60*24))
                .signWith(key) // Especifica la clave y el algoritmo
                .compact();
    }

}

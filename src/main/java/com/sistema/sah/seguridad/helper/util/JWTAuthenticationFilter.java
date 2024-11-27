package com.sistema.sah.seguridad.helper.util;

import com.sistema.sah.seguridad.service.ITokenBlackListService;
import com.sistema.sah.seguridad.service.impl.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Filtro de autenticación JWT.
 * <p>
 * Este filtro se ejecuta una vez por cada solicitud HTTP y realiza las siguientes tareas:
 * <ul>
 *     <li>Extraer y validar el token JWT de la cabecera de autorización.</li>
 *     <li>Verificar si el token está en la lista negra.</li>
 *     <li>Autenticar al usuario si el token es válido y no está en la lista negra.</li>
 * </ul>
 */
@Component
@RequiredArgsConstructor
public class JWTAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final ITokenBlackListService tokenBlackListService;

    /**
     * Método principal del filtro que procesa cada solicitud.
     *
     * @param request     la solicitud HTTP entrante.
     * @param response    la respuesta HTTP.
     * @param filterChain la cadena de filtros para continuar con el procesamiento.
     * @throws ServletException si ocurre un error en el procesamiento del filtro.
     * @throws IOException      si ocurre un error de entrada/salida.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String token = extractTokenFromRequest(request);

        if (token == null) {
            filterChain.doFilter(request, response);
            return;
        }

        if (isTokenBlacklisted(token, response)) {
            return;
        }

        authenticateRequest(token, request);

        filterChain.doFilter(request, response);
    }

    /**
     * Extrae el token JWT de la cabecera de autorización.
     *
     * @param request la solicitud HTTP.
     * @return el token JWT si está presente y tiene el formato correcto, de lo contrario {@code null}.
     */
    private String extractTokenFromRequest(HttpServletRequest request) {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }

    /**
     * Verifica si el token está en la lista negra.
     *
     * @param token    el token JWT a verificar.
     * @param response la respuesta HTTP.
     * @return {@code true} si el token está en la lista negra; de lo contrario, {@code false}.
     * @throws IOException si ocurre un error al escribir la respuesta de error.
     */
    private boolean isTokenBlacklisted(String token, HttpServletResponse response) throws IOException {
        if (tokenBlackListService.isTokenBlackListed(token)) {
            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"error\": \"Token está en la lista negra\"}");
            return true;
        }
        return false;
    }

    /**
     * Autentica la solicitud HTTP si el token JWT es válido.
     *
     * @param token   el token JWT proporcionado.
     * @param request la solicitud HTTP.
     */
    private void authenticateRequest(String token, HttpServletRequest request) {
        String username = jwtService.getUsernameFromToken(token);

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            if (jwtService.isTokenValid(token, userDetails)) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
    }
}

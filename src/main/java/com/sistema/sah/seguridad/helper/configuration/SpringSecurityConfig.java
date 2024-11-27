package com.sistema.sah.seguridad.helper.configuration;

import com.sistema.sah.seguridad.helper.exception.CustomAccessDeniedHandler;
import com.sistema.sah.seguridad.helper.exception.CustomAuthenticationEntryPoint;
import com.sistema.sah.seguridad.helper.util.JWTAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Configuración de seguridad de Spring Security.
 * <p>
 * Configura los permisos de acceso, manejo de excepciones de seguridad, filtros de autenticación
 * y políticas de sesión para proteger los endpoints de la API.
 * </p>
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SpringSecurityConfig {

    private final JWTAuthenticationFilter jwtAuthenticationFilter;
    private final AuthenticationProvider authenticationProvider;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;

    /**
     * Configura la cadena de filtros de seguridad de la aplicación.
     *
     * <ul>
     *     <li>Desactiva CSRF.</li>
     *     <li>Configura endpoints públicos y protegidos.</li>
     *     <li>Establece la política de sesión como STATELESS.</li>
     *     <li>Registra filtros personalizados para manejar JWT.</li>
     * </ul>
     *
     * @param http instancia de {@link HttpSecurity} para configurar la seguridad.
     * @return una instancia de {@link SecurityFilterChain}.
     * @throws Exception si ocurre un error al configurar la seguridad.
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable) // Desactiva CSRF ya que se usa JWT
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/v3/api-docs/**",    // Endpoints públicos relacionados con Swagger
                                "/doc/**",
                                "/usuario/**",       // Endpoints públicos de usuario
                                "/tipo-usuario/**",
                                "/tipo-cuarto/**"
                        ).permitAll()              // Permite acceso público a estas rutas
                        .anyRequest().authenticated() // Todas las demás rutas requieren autenticación
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // Política sin estado
                )
                .exceptionHandling(handler -> handler
                        .authenticationEntryPoint(customAuthenticationEntryPoint) // Maneja errores de autenticación
                        .accessDeniedHandler(customAccessDeniedHandler) // Maneja accesos denegados
                )
                .authenticationProvider(authenticationProvider) // Proveedor de autenticación
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class) // Filtro JWT antes de UsernamePassword
                .build();
    }
}

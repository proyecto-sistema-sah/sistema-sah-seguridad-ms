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

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SpringSecurityConfig {

    private final JWTAuthenticationFilter jwtAuthenticationFilter;

    private final AuthenticationProvider authenticationProvider;

    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;


    /*
    * Filtros para los request para los permisos de acceso o usuo de endpoints del back
    * */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http.csrf(AbstractHttpConfigurer::disable).authorizeHttpRequests(auth ->
            auth
                    .requestMatchers("/v3/api-docs/**",
                            "/doc/**",
                            "/usuario/**",
                            "/tipo-usuario/**",
                            "/tipo-cuarto/**").permitAll()
                    .anyRequest().authenticated()
        ).sessionManagement(sessionManager ->
                                sessionManager.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                        )
                .exceptionHandling(
                        handling -> handling.authenticationEntryPoint(customAuthenticationEntryPoint).accessDeniedHandler(customAccessDeniedHandler)
                )
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}

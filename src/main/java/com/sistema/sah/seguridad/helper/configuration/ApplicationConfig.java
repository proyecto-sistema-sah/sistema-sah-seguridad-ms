package com.sistema.sah.seguridad.helper.configuration;

import com.sistema.sah.commons.entity.UsuarioEntity;
import com.sistema.sah.commons.helper.mapper.UsuarioMapper;
import com.sistema.sah.seguridad.dto.UserSecurityDto;
import com.sistema.sah.seguridad.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuración principal de seguridad y otras configuraciones relacionadas con la aplicación.
 */
@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;

    /**
     * Bean para el gestor de autenticación.
     *
     * @param config configuración de autenticación proporcionada por Spring Security.
     * @return el gestor de autenticación configurado.
     * @throws Exception si ocurre un error en la configuración.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    /**
     * Bean para el proveedor de autenticación.
     *
     * @return el proveedor de autenticación configurado con `UserDetailsService` y codificador de contraseñas.
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService());
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    /**
     * Bean para el codificador de contraseñas.
     *
     * @return instancia de {@link BCryptPasswordEncoder}.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Bean para el servicio de detalles de usuario.
     *
     * @return un servicio que carga los detalles del usuario a partir del repositorio.
     * @throws UsernameNotFoundException si el correo no existe en la base de datos.
     */
    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            UsuarioEntity usuario = usuarioRepository.findByCorreoUsuario(username)
                    .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con el correo: " + username));
            return new UserSecurityDto(usuarioMapper.entityToDto(usuario));
        };
    }

    /**
     * Configuración global de CORS.
     *
     * @return una configuración de {@link WebMvcConfigurer} para permitir solicitudes desde los orígenes especificados.
     */
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins(
                                "http://localhost:4200", // Origen para desarrollo local
                                "https://sistema-sah-front-mr-ewfqbtgbbna7gca2.mexicocentral-01.azurewebsites.net" // Origen de producción
                        )
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("*")
                        .allowCredentials(true); // Permite el uso de cookies y autenticación basada en sesión
            }
        };
    }
}

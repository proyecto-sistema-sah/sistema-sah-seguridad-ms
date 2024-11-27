package com.sistema.sah.seguridad.dto;

import com.sistema.sah.commons.dto.UsuarioDto;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/**
 * DTO que extiende {@link UsuarioDto} y proporciona la implementación de {@link UserDetails}.
 * <p>
 * Este objeto encapsula la información de seguridad del usuario y define
 * los métodos necesarios para integrar con Spring Security.
 * </p>
 */
@Data
public class UserSecurityDto extends UsuarioDto implements UserDetails {

    /**
     * Contraseña del usuario.
     * <p>
     * Este campo es requerido por la interfaz {@link UserDetails}.
     * </p>
     */
    private String password;

    /**
     * Constructor que inicializa el DTO con la información de un {@link UsuarioDto}.
     *
     * @param usuarioDto el objeto {@link UsuarioDto} que contiene la información base del usuario.
     */
    public UserSecurityDto(UsuarioDto usuarioDto) {
        super(
                usuarioDto.getCodigoUsuario(),
                usuarioDto.getNombresUsuario(),
                usuarioDto.getApellidosUsuario(),
                usuarioDto.getCorreoUsuario(),
                usuarioDto.getCodigoImagenUsuario(),
                usuarioDto.getContrasena(),
                usuarioDto.getTipoUsuarioDtoFk()
        );
    }

    /**
     * Obtiene los roles o autoridades asignadas al usuario.
     *
     * @return una colección de {@link GrantedAuthority} con las autoridades del usuario.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(
                super.getTipoUsuarioDtoFk().getNombreTipoUsuario().getDescripcion()
        ));
    }

    /**
     * Devuelve la contraseña del usuario.
     *
     * @return la contraseña como {@link String}.
     */
    @Override
    public String getPassword() {
        return super.getContrasena();
    }

    /**
     * Devuelve el nombre de usuario (correo electrónico en este caso).
     *
     * @return el correo del usuario como {@link String}.
     */
    @Override
    public String getUsername() {
        return super.getCorreoUsuario();
    }

    /**
     * Indica si la cuenta del usuario no ha expirado.
     *
     * @return {@code true}, ya que por defecto las cuentas no expiran.
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * Indica si la cuenta del usuario no está bloqueada.
     *
     * @return {@code true}, ya que por defecto las cuentas no se bloquean.
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * Indica si las credenciales del usuario no han expirado.
     *
     * @return {@code true}, ya que por defecto las credenciales no expiran.
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * Indica si el usuario está habilitado.
     *
     * @return {@code true}, ya que por defecto los usuarios están habilitados.
     */
    @Override
    public boolean isEnabled() {
        return true;
    }
}

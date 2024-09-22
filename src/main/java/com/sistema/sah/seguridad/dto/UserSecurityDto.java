package com.sistema.sah.seguridad.dto;

import com.sistema.sah.commons.dto.UsuarioDto;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Data
public class UserSecurityDto extends UsuarioDto implements UserDetails {

    private String password;

    public UserSecurityDto(UsuarioDto usuarioDto) {
        super(usuarioDto.getCodigoUsuario(),
                usuarioDto.getNombresUsuario(),
                usuarioDto.getApellidosUsuario(),
                usuarioDto.getCorreoUsuario(),
                usuarioDto.getCodigoImagenUsuario(),
                usuarioDto.getContrasena(),
                usuarioDto.getTipoUsuarioDtoFk());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(super.getTipoUsuarioDtoFk().getNombreTipoUsuario().getDescripcion()));
    }

    @Override
    public String getPassword() {
        return super.getContrasena();
    }

    @Override
    public String getUsername() {
        return super.getCorreoUsuario();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}

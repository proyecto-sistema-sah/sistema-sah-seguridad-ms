package com.sistema.sah.seguridad.service;

import com.sistema.sah.commons.dto.RespuestaGeneralDto;
import com.sistema.sah.commons.dto.UsuarioDto;
import com.sistema.sah.seguridad.dto.UserSecurityDto;

public interface ICreateUsuarioService {

    RespuestaGeneralDto saveUsuario(UserSecurityDto usuarioDto);

}

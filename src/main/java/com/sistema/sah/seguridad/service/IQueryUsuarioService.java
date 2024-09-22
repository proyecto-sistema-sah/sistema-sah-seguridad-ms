package com.sistema.sah.seguridad.service;

import com.sistema.sah.commons.dto.RespuestaGeneralDto;
import com.sistema.sah.seguridad.dto.LoginDto;

public interface IQueryUsuarioService {

    RespuestaGeneralDto findAllUsuario();

    RespuestaGeneralDto loginUser(LoginDto loginDto);

}

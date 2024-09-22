package com.sistema.sah.seguridad.controller;

import com.sistema.sah.commons.dto.RespuestaGeneralDto;
import com.sistema.sah.commons.dto.UsuarioDto;
import com.sistema.sah.seguridad.dto.LoginDto;
import com.sistema.sah.seguridad.dto.UserSecurityDto;
import com.sistema.sah.seguridad.service.ICreateUsuarioService;
import com.sistema.sah.seguridad.service.IQueryUsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuario")
@RequiredArgsConstructor
public class UsuarioController {

    private final IQueryUsuarioService usuarioService;

    private final ICreateUsuarioService createUsuarioService;


    @GetMapping("/buscar-todos")
    public ResponseEntity<RespuestaGeneralDto> listadoUsuarios(){
        RespuestaGeneralDto respuestaGeneralDto = usuarioService.findAllUsuario();
        return ResponseEntity.status(respuestaGeneralDto.getStatus()).body(respuestaGeneralDto);
    }

    @PostMapping("/login")
    public ResponseEntity<RespuestaGeneralDto> login(@RequestBody LoginDto loginDto){
        RespuestaGeneralDto respuestaGeneralDto = usuarioService.loginUser(loginDto);
        return ResponseEntity.status(respuestaGeneralDto.getStatus()).body(respuestaGeneralDto);
    }

    @PostMapping("/crear")
    public ResponseEntity<RespuestaGeneralDto> createUsuario(@RequestBody UsuarioDto usuarioDto){
        RespuestaGeneralDto respuestaGeneralDto = createUsuarioService.saveUsuario(new UserSecurityDto(usuarioDto));
        return ResponseEntity.status(respuestaGeneralDto.getStatus()).body(respuestaGeneralDto);
    }
}

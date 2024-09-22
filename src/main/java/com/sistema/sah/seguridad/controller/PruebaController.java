package com.sistema.sah.seguridad.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dos")
public class PruebaController {

    @SecurityRequirement(name = "bearer-key")
    @GetMapping
    public ResponseEntity<String> prueba() {
        return ResponseEntity.ok("Ok");
    }


}

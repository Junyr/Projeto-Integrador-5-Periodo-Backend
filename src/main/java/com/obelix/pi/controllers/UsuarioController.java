package com.obelix.pi.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.obelix.pi.service.interfaces.IUsuarioService;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final IUsuarioService usuarioService;

    public UsuarioController(IUsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    // Exemplo de rota futura (como login)
    // @PostMapping("/login")
    // public ResponseEntity<?> autenticar(...) { ... }
}


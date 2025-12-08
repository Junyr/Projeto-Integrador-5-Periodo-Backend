package com.obelix.pi.controllers;

import com.obelix.pi.query.ConsultaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/consultas")
public class ConsultaController {

    @Autowired
    private ConsultaService consultaService;

    @GetMapping("/consulta-avancada")
    public ResponseEntity<String> consultar(@RequestParam String comando) {

        boolean valido = consultaService.validarComando(comando);

        if (!valido) {
            return ResponseEntity.badRequest().body("Comando inválido!");
        }

        return ResponseEntity.ok("Comando válido!");
    }
}
package com.obelix.pi.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.obelix.pi.model.Rota;
import com.obelix.pi.service.interfaces.IRotaService;

@RestController
@RequestMapping("/api/rotas")
public class RotaController {

    private final IRotaService rotaService;

    public RotaController(IRotaService rotaService) {
        this.rotaService = rotaService;
    }

    @PostMapping
    public ResponseEntity<Void> salvar(@RequestBody Rota rota) {
        rotaService.salvarRota(rota);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    public ResponseEntity<List<Rota>> listar() {
        return ResponseEntity.ok(rotaService.listarRota());
    }
}


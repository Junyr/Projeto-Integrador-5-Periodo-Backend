package com.obelix.pi.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.obelix.pi.model.PontoColeta;
import com.obelix.pi.service.interfaces.IPontoColetaService;

@RestController
@RequestMapping("/api/pontos")
public class PontoColetaController {

    private final IPontoColetaService pontoService;

    public PontoColetaController(IPontoColetaService pontoService) {
        this.pontoService = pontoService;
    }

    @PostMapping
    public ResponseEntity<Void> cadastrar(@RequestBody PontoColeta ponto) {
        pontoService.cadastrarPontoColeta(ponto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    public ResponseEntity<List<PontoColeta>> listar() {
        return ResponseEntity.ok(pontoService.listarPontoColeta());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> atualizar(@PathVariable Long id, @RequestBody PontoColeta ponto) {
        pontoService.atualizarPontoColeta(id, ponto);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        pontoService.deletarPontoColeta(id);
        return ResponseEntity.noContent().build();
    }
}


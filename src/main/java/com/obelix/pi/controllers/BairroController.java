package com.obelix.pi.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.obelix.pi.model.Bairro;
import com.obelix.pi.service.interfaces.IBairroService;

@RestController
@RequestMapping("/api/bairros")
public class BairroController {

    private final IBairroService bairroService;

    public BairroController(IBairroService bairroService) {
        this.bairroService = bairroService;
    }

    @PostMapping
    public ResponseEntity<Void> cadastrar(@RequestBody Bairro bairro) {
        bairroService.cadastrarBairro(bairro);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping
    public ResponseEntity<Void> atualizar(@RequestBody Bairro bairro) {
        bairroService.atualizarBairro(bairro);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        bairroService.deletarBairro(id);
        return ResponseEntity.noContent().build();
    }
}


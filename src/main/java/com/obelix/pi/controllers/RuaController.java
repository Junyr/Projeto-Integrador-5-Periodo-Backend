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

import com.obelix.pi.model.Rua;
import com.obelix.pi.service.interfaces.IRuaService;

@RestController
@RequestMapping("/api/ruas")
public class RuaController {

    private final IRuaService ruaService;

    public RuaController(IRuaService ruaService) {
        this.ruaService = ruaService;
    }

    @PostMapping
    public ResponseEntity<Void> cadastrar(@RequestBody Rua rua) {
        ruaService.cadastrarRua(rua);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping
    public ResponseEntity<Void> atualizar(@RequestBody Rua rua) {
        ruaService.atualizarRua(rua);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        ruaService.deletarRua(id);
        return ResponseEntity.noContent().build();
    }
}


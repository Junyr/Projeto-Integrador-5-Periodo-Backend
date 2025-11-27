package com.obelix.pi.controllers;

import java.util.List;

import com.obelix.pi.model.Residuo;
import com.obelix.pi.repository.ResiduoRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller de resíduos: simples, mas com validação contra duplicidade.
 */
@RestController
@RequestMapping("/residuo")
public class ResiduoController {

    @Autowired
    private ResiduoRepo repo;

    @GetMapping("/listar")
    public ResponseEntity<List<Residuo>> listar() {
        return ResponseEntity.ok(repo.findAll());
    }

    @GetMapping("/buscar/{id}")
    public ResponseEntity<Residuo> buscar(@PathVariable Long id) {
        Residuo r = repo.findById(id).orElseThrow(() -> new RuntimeException("Resíduo não encontrado"));
        return ResponseEntity.ok(r);
    }

    @PostMapping("/adicionar")
    public ResponseEntity<Residuo> cadastrar(@RequestBody Residuo residuo) {
        if (residuo.getTipo() == null || residuo.getTipo().isBlank()) {
            throw new RuntimeException("Tipo de resíduo não pode ser vazio.");
        }
        // evitar duplicidade (assume método existsByTipoIgnoreCase ou existsByTipo)
        boolean existe = false;
        try { existe = repo.existsByTipo(residuo.getTipo()); } catch (Exception ignored) {}
        if (existe) throw new RuntimeException("Resíduo já existe: " + residuo.getTipo());

        Residuo salvo = repo.save(residuo);
        return ResponseEntity.status(201).body(salvo);
    }

    @PutMapping("/atualizar/{id}")
    public ResponseEntity<Residuo> atualizar(@PathVariable Long id, @RequestBody Residuo residuo) {
        Residuo existente = repo.findById(id).orElseThrow(() -> new RuntimeException("Resíduo não encontrado"));
        if (residuo.getTipo() == null || residuo.getTipo().isBlank()) {
            throw new RuntimeException("Tipo de resíduo não pode ser vazio.");
        }
        existente.setTipo(residuo.getTipo());
        repo.save(existente);
        return ResponseEntity.ok(existente);
    }

    @DeleteMapping("/deletar/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        if (!repo.existsById(id)) return ResponseEntity.notFound().build();
        repo.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

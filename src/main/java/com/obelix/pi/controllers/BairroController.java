package com.obelix.pi.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.obelix.pi.model.Bairro;
import com.obelix.pi.repository.BairroRepo;

@RestController
@RequestMapping("/bairros")
public class BairroController {

    @Autowired
    private BairroRepo repo;

    @GetMapping("/listar")
    public ResponseEntity<List<Bairro>> listar() {
        return ResponseEntity.ok(repo.findAll());
    }

    @GetMapping("/buscar/{id}")
    public ResponseEntity<Bairro> buscar(@PathVariable Long id) {
        Bairro b = repo.findById(id).orElseThrow(() -> new RuntimeException("Bairro não encontrado"));
        return ResponseEntity.ok(b);
    }

    @PostMapping("/adicionar")
    public ResponseEntity<Bairro> cadastrar(@RequestBody Bairro bairro) {
        if (bairro.getNome() == null || bairro.getNome().isBlank()) {
            throw new RuntimeException("Nome do bairro não pode ser vazio.");
        }
        Bairro salvo = repo.save(bairro);
        return ResponseEntity.status(201).body(salvo);
    }

    @PutMapping("/atualizar/{id}")
    public ResponseEntity<Bairro> atualizar(@PathVariable Long id, @RequestBody Bairro bairro) {
        Bairro existente = repo.findById(id).orElseThrow(() -> new RuntimeException("Bairro não encontrado"));
        if (bairro.getNome() == null || bairro.getNome().isBlank()) {
            throw new RuntimeException("Nome do bairro não pode ser vazio.");
        }
        existente.setNome(bairro.getNome());
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

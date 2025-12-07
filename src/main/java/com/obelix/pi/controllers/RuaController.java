package com.obelix.pi.controllers;

import java.util.List;

import com.obelix.pi.controllers.DTO.RuaRequestDTO;
import com.obelix.pi.model.Bairro;
import com.obelix.pi.model.Rua;
import com.obelix.pi.repository.BairroRepo;
import com.obelix.pi.repository.RuaRepo;
import com.obelix.pi.service.RotaService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller de Ruas.
 */
@RestController
@RequestMapping("/ruas")
public class RuaController {

    @Autowired
    private RotaService service;

    @Autowired
    private BairroRepo bairroRepo;

    @Autowired
    private RuaRepo repo;

    @GetMapping("/listar")
    public ResponseEntity<List<Rua>> listar() {
        return ResponseEntity.ok(repo.findAll());
    }

    @GetMapping("/buscar/{id}")
    public ResponseEntity<Rua> buscar(@PathVariable Long id) {
        Rua r = repo.findById(id).orElseThrow(() -> new RuntimeException("Rua não encontrada"));
        return ResponseEntity.ok(r);
    }

    @PostMapping("/adicionar")
    public ResponseEntity<Rua> cadastrar(@RequestBody RuaRequestDTO requestDTO) {
        requestDTO.validarAtributos(bairroRepo);
        Bairro origem = bairroRepo.findById(requestDTO.getOrigemId()).orElseThrow(() -> new RuntimeException("Bairro de origem não encontrado"));
        Bairro destino = bairroRepo.findById(requestDTO.getDestinoId()).orElseThrow(() -> new RuntimeException("Bairro de destino não encontrado"));

        Rua rua = new Rua();
        rua.setOrigem(origem);
        rua.setDestino(destino);
        rua.setDistanciaKm(requestDTO.getDistanciaKm());
        repo.save(rua);
        service.atualizarRotas();
        return ResponseEntity.status(201).body(rua);
    }

    @PutMapping("/atualizar/{id}")
    public ResponseEntity<Rua> atualizar(@PathVariable Long id, @RequestBody RuaRequestDTO requestDTO) {
        if (!repo.existsById(id)) throw new RuntimeException("Rua não encontrada");
        requestDTO.validarAtributos(bairroRepo);

        Bairro origem = bairroRepo.findById(requestDTO.getOrigemId()).orElseThrow(() -> new RuntimeException("Bairro de origem não encontrado"));
        Bairro destino = bairroRepo.findById(requestDTO.getDestinoId()).orElseThrow(() -> new RuntimeException("Bairro de destino não encontrado"));

        Rua atualizarRua = repo.findById(id).orElseThrow(() -> new RuntimeException("Rua não encontrada"));
        atualizarRua.setOrigem(origem);
        atualizarRua.setDestino(destino);
        atualizarRua.setDistanciaKm(requestDTO.getDistanciaKm());
        repo.save(atualizarRua);
        service.atualizarRotas();
        return ResponseEntity.ok(atualizarRua);
    }

    @DeleteMapping("/deletar/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        if (!repo.existsById(id)) return ResponseEntity.notFound().build();
        repo.deleteById(id);
        service.atualizarRotas();
        return ResponseEntity.noContent().build();
    }
}

package com.obelix.pi.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.obelix.pi.model.Rota;
import com.obelix.pi.model.Rua;
import com.obelix.pi.repository.RuaRepo;
import com.obelix.pi.service.RotaService;

@RestController
@RequestMapping("/ruas")
public class RuaController {

    @Autowired
    RotaService service;

    @Autowired
    RuaRepo repo;

    @GetMapping("/listar")
    public List<Rua> listar() {
        return repo.findAll();
    }

    @PostMapping("/adicionar")
    public void cadastrar(@RequestBody Rua rua) {
        repo.save(rua);
        service.atualizarRotas();
    }

    @PutMapping("/atualizar/{id}")
    public void atualizar(@PathVariable Long id, @RequestBody Rua rua) {
        if (repo.existsById(id)) {
            Rua atualizarRua = repo.getReferenceById(id);
            atualizarRua.setOrigem(rua.getOrigem());
            atualizarRua.setDestino(rua.getDestino());
            atualizarRua.setDistanciaKm(rua.getDistanciaKm());
            repo.save(atualizarRua);
            service.atualizarRotas();
        } else {
            throw new RuntimeException("Rua não encontrada");
        }
    }

    @DeleteMapping("/deletar/{id}")
    public void deletar(@PathVariable Long id) {
        repo.deleteById(id);
        service.atualizarRotas();
    }
}


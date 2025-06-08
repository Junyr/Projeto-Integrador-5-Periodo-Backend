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
import com.obelix.pi.repository.RotaRepo;

@RestController
@RequestMapping("/rota")
public class RotaController {

    @Autowired
    RotaRepo repo;

    @GetMapping("/listar")
    public List<Rota> listar() {
        return repo.findAll();
    }

    @PostMapping("/adicionar")
    public void cadastrar(@RequestBody Rota rota) {
        repo.save(rota);
    }

    @PutMapping("/atualizar/{id}")
    public void atualizar(@PathVariable Long id, @RequestBody Rota rota) {
        if (repo.existsById(id)) {
            Rota atualizarRota = repo.getReferenceById(id);
            atualizarRota.setCaminhao(rota.getCaminhao());
            atualizarRota.setBairros(rota.getBairros());
            atualizarRota.setRuas(rota.getRuas());
            atualizarRota.setResiduosAtendidos(rota.getResiduosAtendidos());
            atualizarRota.setDistanciaTotal(rota.getDistanciaTotal());
            repo.save(atualizarRota);
        } else {
            throw new RuntimeException("Rota não encontrada");
        }
    }

    @DeleteMapping("/deletar/{id}")
    public void deletar(@PathVariable Long id) {
        repo.deleteById(id);;
    }
}


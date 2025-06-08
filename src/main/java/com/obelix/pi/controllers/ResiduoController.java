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

import com.obelix.pi.model.Residuo;
import com.obelix.pi.repository.ResiduoRepo;

@RestController
@RequestMapping("/residuo")
public class ResiduoController {

    @Autowired
    ResiduoRepo repo;

    @GetMapping("/listar")
    public List<Residuo> listar() {
        return repo.findAll();
    }

    @PostMapping("/adicionar")
    public void cadastrar(@RequestBody Residuo residuo) {
        repo.save(residuo);
    }

    @PutMapping("/atualizar/{id}")
    public void atualizar(@PathVariable Long id, @RequestBody Residuo residuo) {
        if (repo.existsById(id)) {
            Residuo atualizarResiduo = repo.getReferenceById(id);
            atualizarResiduo.setTipo(residuo.getTipo());
            repo.save(atualizarResiduo);
        } else {
            throw new RuntimeException("Residuo não encontrado");
        }
    }

    @DeleteMapping("/deletar/{id}")
    public void deletar(@PathVariable Long id) {
        repo.deleteById(id);;
    }
}

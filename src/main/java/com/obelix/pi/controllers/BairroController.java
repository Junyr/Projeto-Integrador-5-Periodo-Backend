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

import com.obelix.pi.model.Bairro;
import com.obelix.pi.repository.BairroRepo;

@RestController
@RequestMapping("/bairros")
public class BairroController {

    @Autowired
    BairroRepo repo;

    @GetMapping("/listar")
    public List<Bairro> listar() {
        return repo.findAll();
    }

    @PostMapping("/adicionar")
    public void cadastrar(@RequestBody Bairro bairro) {
        repo.save(bairro);
    }

    @PutMapping("/atualizar/{id}")
    public void atualizar(@PathVariable Long id, @RequestBody Bairro bairro) {
        if (repo.existsById(id)) {
            Bairro atualizarBairro = repo.getReferenceById(id);
            atualizarBairro.setNome(bairro.getNome());
            repo.save(atualizarBairro);
        } else {
            throw new RuntimeException("Bairro não encontrado");
        }
    }

    @DeleteMapping("/deletar/{id}")
    public void deletar(@PathVariable Long id) {
        repo.deleteById(id);;
    }
}


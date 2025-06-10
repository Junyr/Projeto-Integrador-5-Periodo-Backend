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

import com.obelix.pi.model.PontoColeta;
import com.obelix.pi.repository.PontoColetaRepo;

@RestController
@RequestMapping("/ponto_coleta")
public class PontoColetaController {

    @Autowired
    PontoColetaRepo repo;

    @GetMapping("/buscar/{id}")
    public PontoColeta buscarPontoColeta(@PathVariable Long id) {
        if (repo.existsById(id)) {
            return repo.getReferenceById(id);
        } else {
            throw new RuntimeException("Caminhao não encontrado");
        }
    }

    @GetMapping("/listar")
    public List<PontoColeta> listar() {
        return repo.findAll();
    }

    @GetMapping("/listarPorTipoResiduo/{tipoResiduoId}")
    public List<PontoColeta> listarPorTipoResiduo(@PathVariable Long tipoResiduoId) {
        return repo.findByTiposResiduos(tipoResiduoId);
    }

    @PostMapping("/adicionar")
    public void cadastrar(@RequestBody PontoColeta pontoColeta) {
        repo.save(pontoColeta);
    }

    @PutMapping("/atualizar/{id}")
    public void atualizar(@PathVariable Long id, @RequestBody PontoColeta PontoColeta) {
        if (repo.existsById(id)) {
            PontoColeta atualizarPontoColeta = repo.getReferenceById(id);
            atualizarPontoColeta.setNome(PontoColeta.getNome());
            atualizarPontoColeta.setResponsavel(PontoColeta.getResponsavel());
            atualizarPontoColeta.setTelefoneResponsavel(PontoColeta.getTelefoneResponsavel());
            atualizarPontoColeta.setEmailResponsavel(PontoColeta.getEmailResponsavel());
            atualizarPontoColeta.setEndereco(PontoColeta.getEndereco());
            atualizarPontoColeta.setBairro(PontoColeta.getBairro());
            atualizarPontoColeta.setHorario(PontoColeta.getHorario());
            atualizarPontoColeta.setTiposResiduos(PontoColeta.getTiposResiduos());
            repo.save(atualizarPontoColeta);
        } else {
            throw new RuntimeException("Ponto de coleta não encontrado");
        }
    }

    @DeleteMapping("/deletar/{id}")
    public void deletar(@PathVariable Long id) {
        repo.deleteById(id);;
    }
}


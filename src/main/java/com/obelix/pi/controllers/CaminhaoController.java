package com.obelix.pi.controllers;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.obelix.pi.model.Caminhao;
import com.obelix.pi.repository.CaminhaoRepo;
import com.obelix.pi.service.CaminhaoService;

@RestController
@RequestMapping("/caminhao")
public class CaminhaoController {

    @Autowired
    CaminhaoService service;

    @Autowired
    CaminhaoRepo repo;

    @GetMapping("/buscar/{id}")
    public Caminhao buscarCaminhao(@PathVariable Long id) {
        if (repo.existsById(id)) {
            return repo.getReferenceById(id);
        } else {
            throw new RuntimeException("Caminhao não encontrado");
        }
    }

    @GetMapping("/listar")
    public List<Caminhao> listar() {
        return repo.findAll();
    }

    @GetMapping("/listar_por_residuo/{tipoResiduoId}")
    public List<Caminhao> listarPorTipoResiduo(@PathVariable Long tipoResiduoId) {
        return repo.findByTipoResiduoId(tipoResiduoId);
    }

    @PostMapping("/adicionar")
    public void cadastrar(@RequestBody Caminhao caminhao) {
        repo.save(caminhao);
    }

    @PutMapping("/atualizar/{id}")
    public void atualizar(@PathVariable Long id, @RequestBody Caminhao caminhao) {
        if (repo.existsById(id)) {
            Caminhao atualizarCaminhao = repo.getReferenceById(id);
            atualizarCaminhao.setPlaca(caminhao.getPlaca());
            atualizarCaminhao.setMotorista(caminhao.getMotorista());
            atualizarCaminhao.setCapacidade(caminhao.getCapacidade());
            atualizarCaminhao.setTiposResiduos(caminhao.getTiposResiduos());
            repo.save(atualizarCaminhao);
        } else {
            throw new RuntimeException("Caminhao não encontrado");
        }
    }

    @DeleteMapping("/deletar/{id}")
    public void deletar(@PathVariable Long id) {
        repo.deleteById(id);;
    }

}

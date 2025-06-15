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

import com.obelix.pi.controllers.DTO.RuaRequestDTO;
import com.obelix.pi.model.Bairro;
import com.obelix.pi.model.Rua;
import com.obelix.pi.repository.BairroRepo;
import com.obelix.pi.repository.RuaRepo;
import com.obelix.pi.service.RotaService;

@RestController
@RequestMapping("/ruas")
public class RuaController {

    @Autowired
    RotaService service;

    @Autowired
    BairroRepo bairroRepo;
    @Autowired
    RuaRepo repo;

    @GetMapping("/listar")
    public List<Rua> listar() {
        return repo.findAll();
    }

    @PostMapping("/adicionar")
    public void cadastrar(@RequestBody RuaRequestDTO requestDTO) {
        if(requestDTO.validarAtributos(bairroRepo)){
            Bairro origem = bairroRepo.getReferenceById(requestDTO.getOrigemId());
            Bairro destino = bairroRepo.getReferenceById(requestDTO.getDestinoId());

            Rua rua = new Rua();
            rua.setOrigem(origem);
            rua.setDestino(destino);
            rua.setDistanciaKm(requestDTO.getDistanciaKm());
            repo.save(rua);
            service.atualizarRotas();
        }
    }

    @PutMapping("/atualizar/{id}")
    public void atualizar(@PathVariable Long id, @RequestBody RuaRequestDTO requestDTO) {
        if (repo.existsById(id)) {
            if(requestDTO.validarAtributos(bairroRepo)){
                Bairro origem = bairroRepo.getReferenceById(requestDTO.getOrigemId());
                Bairro destino = bairroRepo.getReferenceById(requestDTO.getDestinoId());

                Rua atualizarRua = repo.getReferenceById(id);
                atualizarRua.setOrigem(origem);
                atualizarRua.setDestino(destino);
                atualizarRua.setDistanciaKm(requestDTO.getDistanciaKm());
                repo.save(atualizarRua);
                service.atualizarRotas();
            }
        } else throw new RuntimeException("Rua não encontrada");
    }

    @DeleteMapping("/deletar/{id}")
    public void deletar(@PathVariable Long id) {
        repo.deleteById(id);
        service.atualizarRotas();
    }
}


package com.obelix.pi.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.obelix.pi.controllers.DTO.RotaRequestDTO;
import com.obelix.pi.controllers.DTO.RotaResponseDTO;
import com.obelix.pi.model.Rota;
import com.obelix.pi.repository.BairroRepo;
import com.obelix.pi.repository.CaminhaoRepo;
import com.obelix.pi.repository.ResiduoRepo;
import com.obelix.pi.repository.RotaRepo;
import com.obelix.pi.service.RotaService;

@RestController
@RequestMapping("/rota")
public class RotaController {

    @Autowired
    RotaService service;

    @Autowired
    BairroRepo bairroRepo;

    @Autowired
    ResiduoRepo residuoRepo;

    @Autowired
    CaminhaoRepo caminhaoRepo;

    @Autowired
    RotaRepo repo;

    @GetMapping("/listar")
    public List<RotaResponseDTO> listar() {
        return repo.findAll()
                .stream()
                .map(RotaResponseDTO::new)
                .collect(Collectors.toList());
    }

    @PostMapping("/adicionar")
    public void cadastrar(@RequestBody RotaRequestDTO requestDTO) {
        if (requestDTO.validarAtributos(bairroRepo, residuoRepo, caminhaoRepo)) {
            Rota rota = service.gerarRota(requestDTO); 
            repo.save(rota);
        }
    }

    @PutMapping("/atualizar/{id}")
    public void atualizar(@PathVariable Long id, @RequestBody RotaRequestDTO requestDTO) {
        if (repo.existsById(id)) {
            if(requestDTO.validarAtributos(bairroRepo, residuoRepo, caminhaoRepo)){
                Rota rotaOtimizada = service.gerarRota(requestDTO);
                Rota atualizarRota = repo.getReferenceById(id);
                atualizarRota.setCaminhao(rotaOtimizada.getCaminhao());
                atualizarRota.setBairros(rotaOtimizada.getBairros());
                atualizarRota.setRuas(rotaOtimizada.getRuas());
                atualizarRota.setTiposResiduos(rotaOtimizada.getTiposResiduos());
                atualizarRota.setDistanciaTotal(rotaOtimizada.getDistanciaTotal());
                repo.save(atualizarRota);
            }
        } else throw new RuntimeException("Rota não encontrada");
    }

    @DeleteMapping("/deletar/{id}")
    public void deletar(@PathVariable Long id) {
        repo.deleteById(id);;
    }
}


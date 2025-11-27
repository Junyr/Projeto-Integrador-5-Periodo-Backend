package com.obelix.pi.controllers;

import java.util.List;
import java.util.stream.Collectors;

import com.obelix.pi.controllers.DTO.RotaRequestDTO;
import com.obelix.pi.controllers.DTO.RotaResponseDTO;
import com.obelix.pi.model.Rota;
import com.obelix.pi.repository.BairroRepo;
import com.obelix.pi.repository.CaminhaoRepo;
import com.obelix.pi.repository.ResiduoRepo;
import com.obelix.pi.repository.RotaRepo;
import com.obelix.pi.service.RotaService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller de Rota; delega geração de rota para o RotaService (que você já tem).
 */
@RestController
@RequestMapping("/rota")
public class RotaController {

    @Autowired
    private RotaService service;

    @Autowired
    private BairroRepo bairroRepo;

    @Autowired
    private ResiduoRepo residuoRepo;

    @Autowired
    private CaminhaoRepo caminhaoRepo;

    @Autowired
    private RotaRepo repo;

    @GetMapping("/listar")
    public ResponseEntity<List<RotaResponseDTO>> listar() {
        return ResponseEntity.ok(repo.findAll().stream().map(RotaResponseDTO::new).collect(Collectors.toList()));
    }

    @PostMapping("/adicionar")
    public ResponseEntity<RotaResponseDTO> cadastrar(@RequestBody RotaRequestDTO requestDTO) {
        requestDTO.validarAtributos(bairroRepo, residuoRepo, caminhaoRepo);
        Rota rota = service.gerarRota(requestDTO);
        repo.save(rota);
        return ResponseEntity.status(201).body(new RotaResponseDTO(rota));
    }

    @PutMapping("/atualizar/{id}")
    public ResponseEntity<RotaResponseDTO> atualizar(@PathVariable Long id, @RequestBody RotaRequestDTO requestDTO) {
        if (!repo.existsById(id)) throw new RuntimeException("Rota não encontrada");
        requestDTO.validarAtributos(bairroRepo, residuoRepo, caminhaoRepo);
        Rota rotaOtimizada = service.gerarRota(requestDTO);
        Rota atualizarRota = repo.findById(id).orElseThrow(() -> new RuntimeException("Rota não encontrada"));
        atualizarRota.setCaminhao(rotaOtimizada.getCaminhao());
        atualizarRota.setBairros(rotaOtimizada.getBairros());
        atualizarRota.setRuas(rotaOtimizada.getRuas());
        atualizarRota.setTiposResiduos(rotaOtimizada.getTiposResiduos());
        atualizarRota.setDistanciaTotal(rotaOtimizada.getDistanciaTotal());
        repo.save(atualizarRota);
        return ResponseEntity.ok(new RotaResponseDTO(atualizarRota));
    }

    @DeleteMapping("/deletar/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        if (!repo.existsById(id)) return ResponseEntity.notFound().build();
        repo.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

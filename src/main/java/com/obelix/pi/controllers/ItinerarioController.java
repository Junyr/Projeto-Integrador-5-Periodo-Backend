package com.obelix.pi.controllers;

import java.util.List;
import java.util.stream.Collectors;

import com.obelix.pi.controllers.DTO.ItinerarioResponseDTO;
import com.obelix.pi.model.Bairro;
import com.obelix.pi.model.Caminhao;
import com.obelix.pi.model.Itinerario;
import com.obelix.pi.model.Rota;
import com.obelix.pi.repository.ItinerarioRepo;
import com.obelix.pi.repository.RotaRepo;
import com.obelix.pi.service.CaminhaoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller de Itinerários — corrige lógica de disponibilidade do caminhão.
 */
@RestController
@RequestMapping("/itinerario")
public class ItinerarioController {

    @Autowired
    private ItinerarioRepo repo;

    @Autowired
    private CaminhaoService service;

    @Autowired
    private RotaRepo rotaRepo;

    @GetMapping("/listar")
    public ResponseEntity<List<ItinerarioResponseDTO>> listar() {
        return ResponseEntity.ok(repo.findAll().stream().map(ItinerarioResponseDTO::new).collect(Collectors.toList()));
    }

    @GetMapping("/buscar/{id}")
    public ResponseEntity<ItinerarioResponseDTO> buscarCaminhao(@PathVariable Long id) {
        Itinerario itinerario = repo.findById(id).orElseThrow(() -> new RuntimeException("Itinerario não encontrado"));
        return ResponseEntity.ok(new ItinerarioResponseDTO(itinerario));
    }

    @PostMapping("/adicionar")
    public ResponseEntity<ItinerarioResponseDTO> cadastrar(@RequestBody ItinerarioRequestDTO itinerario) {
        if (itinerario == null || itinerario.getRotaId() == null || itinerario.getRotaId() == null) {
            throw new RuntimeException("Rota inválida no itinerário.");
        }
        Rota rota = rotaRepo.findById(itinerario.getRotaId()).orElseThrow(() -> new RuntimeException("Rota não encontrada"));

        // verificarDisponibilidade deve retornar true se disponível
        boolean disponivel = service.verificarDisponibilidade(rota.getCaminhao().getId(), itinerario.getData());
        if (!disponivel) throw new RuntimeException("Caminhão não disponível para a data informada");

        Itinerario novoItinerario = new Itinerario();
        novoItinerario.setData(itinerario.getData());
        novoItinerario.setRota(rota);
        repo.save(novoItinerario);
        return ResponseEntity.status(201).body(new ItinerarioResponseDTO(novoItinerario));
    }

    @PutMapping("/atualizar/{id}")
    public ResponseEntity<ItinerarioResponseDTO> atualizar(@PathVariable Long id, @RequestBody ItinerarioRequestDTO itinerario) {
        if (!repo.existsById(id)) throw new RuntimeException("Itinerário não encontrado.");

        if (itinerario == null || itinerario.getRotaId() == null || itinerario.getRotaId() == null) {
            throw new RuntimeException("Rota inválida no itinerário.");
        }

        Rota rota = rotaRepo.findById(itinerario.getRotaId()).orElseThrow(() -> new RuntimeException("Rota não encontrada"));
        boolean disponivel = service.verificarDisponibilidade(rota.getCaminhao().getId(), itinerario.getData());
        if (!disponivel) throw new RuntimeException("Caminhão não disponível para a data informada");

        Itinerario atualizarIt = repo.findById(id).orElseThrow(() -> new RuntimeException("Itinerário não encontrado"));
        atualizarIt.setData(itinerario.getData());
        atualizarIt.setRota(rota);
        repo.save(atualizarIt);
        return ResponseEntity.ok(new ItinerarioResponseDTO(atualizarIt));
    }

    @DeleteMapping("/deletar/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        if (!repo.existsById(id)) return ResponseEntity.notFound().build();
        repo.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

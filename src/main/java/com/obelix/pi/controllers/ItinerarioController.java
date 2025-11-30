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

import com.obelix.pi.controllers.DTO.ItinerarioResponseDTO;
import com.obelix.pi.model.Itinerario;
import com.obelix.pi.model.Rota;
import com.obelix.pi.repository.ItinerarioRepo;
import com.obelix.pi.repository.RotaRepo;
import com.obelix.pi.service.CaminhaoService;

import org.springframework.http.ResponseEntity;

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

    @PostMapping("/adicionar")
    public ResponseEntity<ItinerarioResponseDTO> cadastrar(@RequestBody Itinerario itinerario) {
        if (itinerario == null || itinerario.getRota() == null || itinerario.getRota().getId() == null) {
            throw new RuntimeException("Rota inválida no itinerário.");
        }
        Rota rota = rotaRepo.findById(itinerario.getRota().getId()).orElseThrow(() -> new RuntimeException("Rota não encontrada"));

        // verificarDisponibilidade deve retornar true se disponível
        boolean disponivel = service.verificarDisponibilidade(rota.getCaminhao().getId(), itinerario.getData());
        if (!disponivel) throw new RuntimeException("Caminhão não disponível para a data informada");

        itinerario.setRota(rota);
        repo.save(itinerario);
        return ResponseEntity.status(201).body(new ItinerarioResponseDTO(itinerario));
    }

    @PutMapping("/atualizar/{id}")
    public ResponseEntity<ItinerarioResponseDTO> atualizar(@PathVariable Long id, @RequestBody Itinerario itinerario) {
        if (!repo.existsById(id)) throw new RuntimeException("Itinerário não encontrado.");

        if (itinerario == null || itinerario.getRota() == null || itinerario.getRota().getId() == null) {
            throw new RuntimeException("Rota inválida no itinerário.");
        }

        Rota rota = rotaRepo.findById(itinerario.getRota().getId()).orElseThrow(() -> new RuntimeException("Rota não encontrada"));
        boolean disponivel = service.verificarDisponibilidade(rota.getCaminhao().getId(), itinerario.getData());
        if (!disponivel) throw new RuntimeException("Caminhão não disponível para a data informada");

        Itinerario atualizarItinerario = repo.findById(id).orElseThrow(() -> new RuntimeException("Itinerário não encontrado"));
        atualizarItinerario.setData(itinerario.getData());
        atualizarItinerario.setRota(rota);
        repo.save(atualizarItinerario);
        return ResponseEntity.ok(new ItinerarioResponseDTO(atualizarItinerario));
    }

    @DeleteMapping("/deletar/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        if (!repo.existsById(id)) return ResponseEntity.notFound().build();
        repo.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

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

import com.obelix.pi.model.Itinerario;
import com.obelix.pi.repository.ItinerarioRepo;
import com.obelix.pi.repository.RotaRepo;
import com.obelix.pi.service.CaminhaoService;

@RestController
@RequestMapping("/itinerario")
public class ItinerarioController {

    @Autowired
    ItinerarioRepo repo;

    @Autowired
    CaminhaoService service;

    @Autowired
    RotaRepo rotaRepo;

    @GetMapping("/listar")
    public List<Itinerario> listar() {
        return repo.findAll();
    }

    @PostMapping("/adicionar")
    public void cadastrar(@RequestBody Itinerario itinerario) {
        if(rotaRepo.existsById(itinerario.getRota().getId())){
            if(service.verificarDisponibilidade(itinerario.getRota().getCaminhao().getId(), itinerario.getData())) {
                repo.save(itinerario);
            } else throw new RuntimeException("Caminhão não disponível para a data informada");
        } else throw new RuntimeException("Rota não encontrada");
    }

    @PutMapping("/atualizar/{id}")
    public void atualizar(@PathVariable Long id, @RequestBody Itinerario itinerario) {
        if (repo.existsById(id)) {
            Itinerario atualizarItinerario = repo.getReferenceById(id);
            atualizarItinerario.setData(itinerario.getData());
            atualizarItinerario.setRota(itinerario.getRota());
            repo.save(atualizarItinerario);
        } else {
            throw new RuntimeException("Rota não encontrada");
        }
    }

    @DeleteMapping("/deletar/{id}")
    public void deletar(@PathVariable Long id) {
        repo.deleteById(id);;
    }
}


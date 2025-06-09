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
import com.obelix.pi.model.Rota;
import com.obelix.pi.repository.RotaRepo;
import com.obelix.pi.service.RotaService;

@RestController
@RequestMapping("/rota")
public class RotaController {

    @Autowired
    RotaService service;

    @Autowired
    RotaRepo repo;

    @GetMapping("/listar")
    public List<Rota> listar() {
        return repo.findAll();
    }

    @PostMapping("/adicionar")
    public void cadastrar(@RequestBody Long pontoColetaOrigemId, @RequestBody Long pontoColetaDestinoId,
    @RequestBody Residuo tipoResiduo, @RequestBody Long caminhaoId) {
        Rota rota = service.gerarRota(caminhaoId, pontoColetaOrigemId, pontoColetaDestinoId, tipoResiduo); 
        repo.save(rota);
    }

    @PutMapping("/atualizar/{id}")
    public void atualizar(@PathVariable Long rotaId, @RequestBody Long pontoColetaOrigemId, @RequestBody Long pontoColetaDestinoId,
    @RequestBody Residuo tipoResiduo, @RequestBody Long caminhaoId) {
        if (repo.existsById(rotaId)) {
            Rota rotaOtimizada = service.gerarRota(caminhaoId, pontoColetaOrigemId, pontoColetaDestinoId, tipoResiduo);
            Rota atualizarRota = repo.getReferenceById(rotaId);
            atualizarRota.setCaminhao(rotaOtimizada.getCaminhao());
            atualizarRota.setBairros(rotaOtimizada.getBairros());
            atualizarRota.setRuas(rotaOtimizada.getRuas());
            atualizarRota.setTipoResiduo(rotaOtimizada.getTipoResiduo());
            atualizarRota.setDistanciaTotal(rotaOtimizada.getDistanciaTotal());
            repo.save(atualizarRota);
        } else {
            throw new RuntimeException("Rota não encontrada");
        }
    }

    @DeleteMapping("/deletar/{id}")
    public void deletar(@PathVariable Long id) {
        repo.deleteById(id);;
    }
}


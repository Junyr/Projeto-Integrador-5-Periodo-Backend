package com.obelix.pi.controllers;

import java.util.ArrayList;
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

import com.obelix.pi.controllers.DTO.CaminhaoRequestDTO;
import com.obelix.pi.controllers.DTO.CaminhaoResponseDTO;
import com.obelix.pi.model.Caminhao;
import com.obelix.pi.model.Residuo;
import com.obelix.pi.repository.CaminhaoRepo;
import com.obelix.pi.repository.ResiduoRepo;

import org.springframework.http.ResponseEntity;

/**
 * Controller de caminhões — corrige riscos de NPE e usa findById em vez de getReference direto.
 */
@RestController
@RequestMapping("/caminhao")
public class CaminhaoController {

    @Autowired
    private ResiduoRepo residuoRepo;

    @Autowired
    private CaminhaoRepo repo;

    @GetMapping("/buscar/{id}")
    public ResponseEntity<CaminhaoResponseDTO> buscarCaminhao(@PathVariable Long id) {
        Caminhao caminhao = repo.findById(id).orElseThrow(() -> new RuntimeException("Caminhão não encontrado"));
        return ResponseEntity.ok(new CaminhaoResponseDTO(caminhao));
    }

    @GetMapping("/listar")
    public ResponseEntity<List<CaminhaoResponseDTO>> listar() {
        List<CaminhaoResponseDTO> lista = repo.findAll().stream()
            .map(CaminhaoResponseDTO::new)
            .collect(Collectors.toList());
        return ResponseEntity.ok(lista);
    }

    @PostMapping("/adicionar")
    public ResponseEntity<CaminhaoResponseDTO> cadastrar(@RequestBody CaminhaoRequestDTO requestDTO) {
        // valida atributos com o método existente
        requestDTO.validarAtributos();

        // converte ids de resíduos em entidades (verifica existência)
        List<Residuo> listaResiduo = new ArrayList<>();
        for (Long residuoId : requestDTO.getTiposResiduos()) {
            Residuo r = residuoRepo.findById(residuoId).orElseThrow(() -> new RuntimeException("Resíduo não encontrado: id=" + residuoId));
            listaResiduo.add(r);
        }

        Caminhao caminhao = new Caminhao();
        caminhao.setPlaca(requestDTO.getPlaca());
        caminhao.setMotorista(requestDTO.getMotorista());
        caminhao.setCapacidade(requestDTO.getCapacidade());
        caminhao.setTiposResiduos(listaResiduo);
        repo.save(caminhao);

        return ResponseEntity.status(201).body(new CaminhaoResponseDTO(caminhao));
    }

    @PutMapping("/atualizar/{id}")
    public ResponseEntity<CaminhaoResponseDTO> atualizar(@PathVariable Long id, @RequestBody CaminhaoRequestDTO requestDTO) {
        if (!repo.existsById(id)) throw new RuntimeException("Caminhão não encontrado");
        requestDTO.validarAtributos();

        Caminhao atualizarCaminhao = repo.findById(id).orElseThrow(() -> new RuntimeException("Caminhão não encontrado"));
        atualizarCaminhao.setPlaca(requestDTO.getPlaca());
        atualizarCaminhao.setMotorista(requestDTO.getMotorista());
        atualizarCaminhao.setCapacidade(requestDTO.getCapacidade());

        List<Residuo> listaResiduo = new ArrayList<>();
        for (Long residuoId : requestDTO.getTiposResiduos()) {
            Residuo r = residuoRepo.findById(residuoId).orElseThrow(() -> new RuntimeException("Resíduo não encontrado: id=" + residuoId));
            listaResiduo.add(r);
        }
        atualizarCaminhao.setTiposResiduos(listaResiduo);
        repo.save(atualizarCaminhao);

        return ResponseEntity.ok(new CaminhaoResponseDTO(atualizarCaminhao));
    }

    @DeleteMapping("/deletar/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        if (!repo.existsById(id)) return ResponseEntity.notFound().build();
        repo.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

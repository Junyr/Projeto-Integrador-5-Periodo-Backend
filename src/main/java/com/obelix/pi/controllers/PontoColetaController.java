package com.obelix.pi.controllers;

import java.util.List;
import java.util.stream.Collectors;

import com.obelix.pi.controllers.DTO.PontoColetaRequestDTO;
import com.obelix.pi.controllers.DTO.PontoColetaResponseDTO;
import com.obelix.pi.model.PontoColeta;
import com.obelix.pi.model.Residuo;
import com.obelix.pi.repository.BairroRepo;
import com.obelix.pi.repository.PontoColetaRepo;
import com.obelix.pi.repository.ResiduoRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller de pontos de coleta — valida e converte DTOs com segurança.
 */
@RestController
@RequestMapping("/ponto_coleta")
public class PontoColetaController {

    @Autowired
    private PontoColetaRepo repo;

    @Autowired
    private BairroRepo bairroRepo;

    @Autowired
    private ResiduoRepo residuoRepo;

    @GetMapping("/buscar/{id}")
    public ResponseEntity<PontoColetaResponseDTO> buscarPontoColeta(@PathVariable Long id) {
        PontoColeta p = repo.findById(id).orElseThrow(() -> new RuntimeException("Ponto de coleta não encontrado"));
        return ResponseEntity.ok(new PontoColetaResponseDTO(p));
    }

    @GetMapping("/listar")
    public ResponseEntity<List<PontoColetaResponseDTO>> listar() {
        return ResponseEntity.ok(repo.findAll().stream().map(PontoColetaResponseDTO::new).collect(Collectors.toList()));
    }

    @PostMapping("/adicionar")
    public ResponseEntity<PontoColetaResponseDTO> cadastrar(@RequestBody PontoColetaRequestDTO requestDTO) {
        requestDTO.validarAtributos(bairroRepo);

        // converte ids de resíduos para entidades
        List<Residuo> residuos = residuoRepo.findAllById(requestDTO.getTiposResiduos());
        if (residuos == null || residuos.isEmpty()) throw new RuntimeException("Tipos de resíduos inválidos");

        PontoColeta pontoColeta = new PontoColeta();
        pontoColeta.setNome(requestDTO.getNome());
        pontoColeta.setResponsavel(requestDTO.getResponsavel());
        pontoColeta.setTelefoneResponsavel(requestDTO.getTelefoneResponsavel());
        pontoColeta.setEmailResponsavel(requestDTO.getEmailResponsavel());
        pontoColeta.setEndereco(requestDTO.getEndereco());
        pontoColeta.setHorario(requestDTO.getHorario());
        pontoColeta.setBairro(bairroRepo.findById(requestDTO.getBairroId()).orElseThrow(() -> new RuntimeException("Bairro não encontrado")));
        pontoColeta.setTiposResiduos(residuos);
        repo.save(pontoColeta);

        return ResponseEntity.status(201).body(new PontoColetaResponseDTO(pontoColeta));
    }

    @PutMapping("/atualizar/{id}")
    public ResponseEntity<PontoColetaResponseDTO> atualizar(@PathVariable Long id, @RequestBody PontoColetaRequestDTO requestDTO) {
        if (!repo.existsById(id)) throw new RuntimeException("Ponto de coleta não encontrado");
        requestDTO.validarAtributos(bairroRepo);

        PontoColeta atualizar = repo.findById(id).orElseThrow(() -> new RuntimeException("Ponto de coleta não encontrado"));

        List<Residuo> residuos = residuoRepo.findAllById(requestDTO.getTiposResiduos());
        if (residuos == null || residuos.isEmpty()) throw new RuntimeException("Tipos de resíduos inválidos");

        atualizar.setNome(requestDTO.getNome());
        atualizar.setResponsavel(requestDTO.getResponsavel());
        atualizar.setTelefoneResponsavel(requestDTO.getTelefoneResponsavel());
        atualizar.setEmailResponsavel(requestDTO.getEmailResponsavel());
        atualizar.setEndereco(requestDTO.getEndereco());
        atualizar.setHorario(requestDTO.getHorario());
        atualizar.setBairro(bairroRepo.findById(requestDTO.getBairroId()).orElseThrow(() -> new RuntimeException("Bairro não encontrado")));
        atualizar.setTiposResiduos(residuos);
        repo.save(atualizar);

        return ResponseEntity.ok(new PontoColetaResponseDTO(atualizar));
    }

    @DeleteMapping("/deletar/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        if (!repo.existsById(id)) return ResponseEntity.notFound().build();
        repo.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

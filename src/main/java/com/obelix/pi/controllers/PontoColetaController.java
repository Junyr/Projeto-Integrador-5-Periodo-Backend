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

import com.obelix.pi.controllers.DTO.PontoColetaRequestDTO;
import com.obelix.pi.controllers.DTO.PontoColetaResponseDTO;
import com.obelix.pi.model.Bairro;
import com.obelix.pi.model.PontoColeta;
import com.obelix.pi.model.Residuo;
import com.obelix.pi.repository.BairroRepo;
import com.obelix.pi.repository.PontoColetaRepo;
import com.obelix.pi.repository.ResiduoRepo;

@RestController
@RequestMapping("/ponto_coleta")
public class PontoColetaController {

    @Autowired
    PontoColetaRepo repo;

    @Autowired
    BairroRepo bairroRepo;

    @Autowired
    ResiduoRepo residuoRepo;

    @GetMapping("/buscar/{id}")
    public PontoColeta buscarPontoColeta(@PathVariable Long id) {
        if (repo.existsById(id)) {
            return repo.getReferenceById(id);
        } else {
            throw new RuntimeException("Caminhao não encontrado");
        }
    }

    @GetMapping("/listar")
    public List<PontoColetaResponseDTO> listar() {
        return repo.findAll().stream()
            .map(PontoColetaResponseDTO::new)
            .collect(Collectors.toList());
    }

    @GetMapping("/listarPorTipoResiduo/{tipoResiduoId}")
    public List<PontoColeta> listarPorTipoResiduo(@PathVariable Long tipoResiduoId) {
        return repo.findByTiposResiduos(tipoResiduoId);
    }

    @PostMapping("/adicionar")
    public void cadastrar(@RequestBody PontoColetaRequestDTO requestDTO) {
        if(requestDTO.validarAtributos(bairroRepo)){
            PontoColeta pontoColeta = new PontoColeta();
            pontoColeta.setNome(requestDTO.getNome());
            pontoColeta.setResponsavel(requestDTO.getResponsavel());
            pontoColeta.setTelefoneResponsavel(requestDTO.getTelefoneResponsavel());
            pontoColeta.setEmailResponsavel(requestDTO.getEmailResponsavel());
            pontoColeta.setEndereco(requestDTO.getEndereco());
            pontoColeta.setBairro(bairroRepo.getReferenceById(requestDTO.getBairroId()));
            pontoColeta.setHorario(requestDTO.getHorario());
            pontoColeta.setTiposResiduos(residuoRepo.findAllById(requestDTO.getTiposResiduos()));
            repo.save(pontoColeta);
        }
    }

    @PutMapping("/atualizar/{id}")
    public void atualizar(@PathVariable Long id, @RequestBody PontoColetaRequestDTO requestDTO) {
        if (repo.existsById(id)) {
            if(requestDTO.validarAtributos(bairroRepo)){
                PontoColeta atualizarPontoColeta = repo.getReferenceById(id);
                atualizarPontoColeta.setNome(requestDTO.getNome());
                atualizarPontoColeta.setResponsavel(requestDTO.getResponsavel());
                atualizarPontoColeta.setTelefoneResponsavel(requestDTO.getTelefoneResponsavel());
                atualizarPontoColeta.setEmailResponsavel(requestDTO.getEmailResponsavel());
                atualizarPontoColeta.setEndereco(requestDTO.getEndereco());
                atualizarPontoColeta.setBairro(bairroRepo.getReferenceById(requestDTO.getBairroId()));
                atualizarPontoColeta.setHorario(requestDTO.getHorario());
                atualizarPontoColeta.setTiposResiduos(residuoRepo.findAllById(requestDTO.getTiposResiduos()));
                repo.save(atualizarPontoColeta);
            }
        } else throw new RuntimeException("Ponto de coleta não encontrado");
    }

    @DeleteMapping("/deletar/{id}")
    public void deletar(@PathVariable Long id) {
        repo.deleteById(id);;
    }
}


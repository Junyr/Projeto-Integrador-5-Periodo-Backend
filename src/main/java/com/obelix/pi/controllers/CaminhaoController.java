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
import com.obelix.pi.service.CaminhaoService;

@RestController
@RequestMapping("/caminhao")
public class CaminhaoController {

    @Autowired
    CaminhaoService service;

    @Autowired
    ResiduoRepo residuoRepo;

    @Autowired
    CaminhaoRepo repo;

    @GetMapping("/buscar/{id}")
    public CaminhaoResponseDTO buscarCaminhao(@PathVariable Long id) {
        if (repo.existsById(id)) {
            Caminhao caminhao = repo.getReferenceById(id);
            return new CaminhaoResponseDTO(caminhao);
        } else {
            throw new RuntimeException("Caminhao não encontrado");
        }
    }

    @GetMapping("/listar")
    public List<CaminhaoResponseDTO> listar() {
        return repo.findAll().stream()
            .map(CaminhaoResponseDTO::new)
            .collect(Collectors.toList());
    }

    @GetMapping("/listar_por_residuo/{tipoResiduoId}")
    public List<Caminhao> listarPorTipoResiduo(@PathVariable Long tipoResiduoId) {
        return repo.findByTiposResiduos(tipoResiduoId);
    }

    @PostMapping("/adicionar")
    public void cadastrar(@RequestBody CaminhaoRequestDTO requestDTO) {
        if(requestDTO.validarAtributos()) {
            Caminhao caminhao = new Caminhao();
            caminhao.setPlaca(requestDTO.getPlaca());
            caminhao.setMotorista(requestDTO.getMotorista());
            caminhao.setCapacidade(requestDTO.getCapacidade());
            List<Residuo> listaResiduo = new ArrayList<>();
            for (Long residuoId : requestDTO.getTiposResiduos()) {
                listaResiduo.add(residuoRepo.getReferenceById(residuoId));
            }
            caminhao.setTiposResiduos(listaResiduo);
            repo.save(caminhao);
        }
    }

    @PutMapping("/atualizar/{id}")
    public void atualizar(@PathVariable Long id, @RequestBody CaminhaoRequestDTO requestDTO) {
        if (repo.existsById(id)) {
            if(requestDTO.validarAtributos()){
                Caminhao atualizarCaminhao = repo.getReferenceById(id);
                atualizarCaminhao.setPlaca(requestDTO.getPlaca());
                atualizarCaminhao.setMotorista(requestDTO.getMotorista());
                atualizarCaminhao.setCapacidade(requestDTO.getCapacidade());
                List<Residuo> listaResiduo = new ArrayList<>();
                for (Long residuoId : requestDTO.getTiposResiduos()) {
                    listaResiduo.add(residuoRepo.getReferenceById(residuoId));
                }
                atualizarCaminhao.setTiposResiduos(listaResiduo);
                repo.save(atualizarCaminhao);
            }
        } else {
            throw new RuntimeException("Caminhao não encontrado");
        }
    }

    @DeleteMapping("/deletar/{id}")
    public void deletar(@PathVariable Long id) {
        repo.deleteById(id);;
    }

}

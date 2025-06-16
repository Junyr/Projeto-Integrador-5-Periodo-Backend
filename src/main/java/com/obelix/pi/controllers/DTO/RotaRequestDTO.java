package com.obelix.pi.controllers.DTO;

import java.util.List;

import com.obelix.pi.repository.BairroRepo;
import com.obelix.pi.repository.CaminhaoRepo;
import com.obelix.pi.repository.ResiduoRepo;

import lombok.Data;

@Data
public class RotaRequestDTO {
    private Long origemId;
    private Long destinoId;
    private List<Long> tipoResiduoId;
    private Long caminhaoId;

    public boolean validarAtributos(BairroRepo bairroRepo, ResiduoRepo residuoRepo, CaminhaoRepo caminhaoRepo){
        if(bairroRepo.existsById(origemId)){
            if(bairroRepo.existsById(destinoId)){
                if(tipoResiduoId.size() >= 1){
                    if(caminhaoRepo.existsById(caminhaoId)){
                        return true;
                    } else throw new RuntimeException("Caminhão não encontrado.");
                } else throw new RuntimeException("Informe ao menos um tipo de resíduo.");
            } else throw new RuntimeException("Ponto de coleta destino não encontrado.");
        } else throw new RuntimeException("Ponto de coleta origem não encontrado.");
    }
}

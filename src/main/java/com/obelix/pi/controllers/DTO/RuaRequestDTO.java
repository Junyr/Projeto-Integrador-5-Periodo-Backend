package com.obelix.pi.controllers.DTO;

import com.obelix.pi.repository.BairroRepo;

import lombok.Data;

@Data
public class RuaRequestDTO {
    private Long id;
    private Long origemId;
    private Long destinoId;
    private double distanciaKm;

    public boolean validarAtributos(BairroRepo bairroRepo){
        if(this.origemId == null || this.destinoId == null || this.distanciaKm == 0 ||
         this.origemId == 0 || this.destinoId == 0) {
            throw new RuntimeException("Por favor preencha todos os campos obrigatórios: Bairro de origem e destino, destino Km.");
        }

        if(bairroRepo.existsById(this.origemId)){
            if(bairroRepo.existsById(this.destinoId)){
                if(this.distanciaKm > 0){
                    return true;
                } else throw new RuntimeException("Infome um numero valido para a distancia!");
            } else throw new RuntimeException("Bairro de destino não existe!");
        } else throw new RuntimeException("Bairro de origem não existe!");
    }

}

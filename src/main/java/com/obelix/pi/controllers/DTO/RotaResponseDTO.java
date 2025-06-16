package com.obelix.pi.controllers.DTO;

import java.util.List;
import java.util.stream.Collectors;

import com.obelix.pi.model.Rota;

import lombok.Data;

@Data
public class RotaResponseDTO {
    private Long id;
    private Long caminhaoId;
    private List<Long> bairros;
    private List<Long> ruas;
    private List<Long> tiposResiduos;
    private double distanciaTotal;

    public RotaResponseDTO(Rota rota) {
        this.id = rota.getId();
        this.caminhaoId = rota.getCaminhao().getId();
        this.bairros = rota.getBairros()
                .stream()
                .map(bairro -> bairro.getId())
                .collect(Collectors.toList());
        
        this.ruas = rota.getRuas()
                .stream()
                .map(rua -> rua.getId())
                .collect(Collectors.toList());

        this.tiposResiduos = rota.getTiposResiduos()
                .stream()
                .map(residuo -> residuo.getId())
                .collect(Collectors.toList());

        this.distanciaTotal = rota.getDistanciaTotal();
    }
}

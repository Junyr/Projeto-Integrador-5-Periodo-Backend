package com.obelix.pi.controllers.DTO;

import java.util.List;
import java.util.stream.Collectors;

import com.obelix.pi.model.Caminhao;

import lombok.Data;

@Data
public class CaminhaoResponseDTO {
    private Long id;
    private String placa;
    private String motorista;
    private double capacidade;
    private List<Long> tiposResiduos;

    public CaminhaoResponseDTO(Caminhao caminhao) {
        this.id = caminhao.getId();
        this.placa = caminhao.getPlaca();
        this.motorista = caminhao.getMotorista();
        this.capacidade = caminhao.getCapacidade();
        this.tiposResiduos = caminhao.getTiposResiduos()
            .stream()
            .map(residuo -> residuo.getId())
            .collect(Collectors.toList());
    }
}

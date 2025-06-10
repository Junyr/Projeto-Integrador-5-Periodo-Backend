package com.obelix.pi.controllers.DTO;

import com.obelix.pi.model.Residuo;

import lombok.Data;

@Data
public class RotaRequestDTO {
    private Long pontoColetaOrigemId;
    private Long pontoColetaDestinoId;
    private Residuo tipoResiduo;
    private Long caminhaoId;
}

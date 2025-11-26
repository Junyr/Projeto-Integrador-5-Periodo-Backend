package com.obelix.pi.controllers.DTO;

import java.util.List;

import com.obelix.pi.repository.BairroRepo;
import com.obelix.pi.repository.CaminhaoRepo;
import com.obelix.pi.repository.ResiduoRepo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RotaRequestDTO {

    private Long origemId;
    private Long destinoId;
    private List<Long> tipoResiduoId;
    private Long caminhaoId;

    /**
     * Valida os atributos do DTO.
     * 
     * @param bairroRepo   Repositório de bairros
     * @param residuoRepo  Repositório de resíduos
     * @param caminhaoRepo Repositório de caminhões
     * @return true se todos os atributos forem válidos
     * @throws RuntimeException se algum atributo for inválido
     */
    public boolean validarAtributos(BairroRepo bairroRepo, ResiduoRepo residuoRepo, CaminhaoRepo caminhaoRepo) {
        if (!bairroRepo.existsById(origemId)) {
            throw new RuntimeException("Ponto de coleta origem não encontrado.");
        }
        if (!bairroRepo.existsById(destinoId)) {
            throw new RuntimeException("Ponto de coleta destino não encontrado.");
        }
        if (tipoResiduoId == null || tipoResiduoId.isEmpty()) {
            throw new RuntimeException("Informe ao menos um tipo de resíduo.");
        }
        if (!caminhaoRepo.existsById(caminhaoId)) {
            throw new RuntimeException("Caminhão não encontrado.");
        }
        return true;
    }
}

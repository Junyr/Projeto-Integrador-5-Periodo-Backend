package com.obelix.pi.controllers.DTO;

import java.util.List;

import com.obelix.pi.repository.BairroRepo;
import com.obelix.pi.repository.CaminhaoRepo;
import com.obelix.pi.repository.ResiduoRepo;

/**
 * DTO de requisição para gerar/atualizar Rota.
 */
public class RotaRequestDTO {
    private Long origemId;
    private Long destinoId;
    private List<Long> tipoResiduoId;
    private Long caminhaoId;

    // Getters e Setters
    public Long getOrigemId() { return origemId; }
    public void setOrigemId(Long origemId) { this.origemId = origemId; }

    public Long getDestinoId() { return destinoId; }
    public void setDestinoId(Long destinoId) { this.destinoId = destinoId; }

    public List<Long> getTipoResiduoId() { return tipoResiduoId; }
    public void setTipoResiduoId(List<Long> tipoResiduoId) { this.tipoResiduoId = tipoResiduoId; }

    public Long getCaminhaoId() { return caminhaoId; }
    public void setCaminhaoId(Long caminhaoId) { this.caminhaoId = caminhaoId; }

    /**
     * Valida os atributos do DTO.
     */
    public boolean validarAtributos(BairroRepo bairroRepo, ResiduoRepo residuoRepo, CaminhaoRepo caminhaoRepo) {
        if (origemId == null || destinoId == null || caminhaoId == null || tipoResiduoId == null || tipoResiduoId.isEmpty()) {
            throw new RuntimeException("Dados incompletos da rota.");
        }
        if (!bairroRepo.existsById(origemId)) {
            throw new RuntimeException("Ponto de coleta origem não encontrado.");
        }
        if (!bairroRepo.existsById(destinoId)) {
            throw new RuntimeException("Ponto de coleta destino não encontrado.");
        }
        if (!caminhaoRepo.existsById(caminhaoId)) {
            throw new RuntimeException("Caminhão não encontrado.");
        }
        // opcional: verificar se cada residuo existe
        for (Long id : tipoResiduoId) {
            if (id == null) throw new RuntimeException("ID de resíduo inválido.");
            if (!residuoRepo.existsById(id)) throw new RuntimeException("Resíduo não encontrado: id=" + id);
        }
        return true;
    }
}

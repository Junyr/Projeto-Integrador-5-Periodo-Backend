package com.obelix.pi.controllers.DTO;

import com.obelix.pi.repository.BairroRepo;

/**
 * DTO para criação/atualização de Rua.
 */
public class RuaRequestDTO {

    private Long id;
    private Long origemId;
    private Long destinoId;
    private double distanciaKm;

    public RuaRequestDTO() {}

    // Getters / Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getOrigemId() { return origemId; }
    public void setOrigemId(Long origemId) { this.origemId = origemId; }

    public Long getDestinoId() { return destinoId; }
    public void setDestinoId(Long destinoId) { this.destinoId = destinoId; }

    public double getDistanciaKm() { return distanciaKm; }
    public void setDistanciaKm(double distanciaKm) { this.distanciaKm = distanciaKm; }

    public boolean validarAtributos(BairroRepo bairroRepo) {
        if (this.origemId == null || this.destinoId == null) {
            throw new RuntimeException("Por favor preencha os ids de origem e destino.");
        }
        if (this.distanciaKm <= 0) {
            throw new RuntimeException("Informe um número válido para a distância (maior que zero).");
        }
        if (!bairroRepo.existsById(this.origemId)) {
            throw new RuntimeException("Bairro de origem não existe!");
        }
        if (!bairroRepo.existsById(this.destinoId)) {
            throw new RuntimeException("Bairro de destino não existe!");
        }
        return true;
    }
}

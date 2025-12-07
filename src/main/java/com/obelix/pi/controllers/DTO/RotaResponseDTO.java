package com.obelix.pi.controllers.DTO;

import java.util.ArrayList;
import java.util.List;

import com.obelix.pi.model.Bairro;
import com.obelix.pi.model.Residuo;
import com.obelix.pi.model.Rota;
import com.obelix.pi.model.Rua;

/**
 * DTO de resposta para Rota.
 */
public class RotaResponseDTO {

    private Long id;
    private Long caminhaoId;
    private List<Long> bairros = new ArrayList<>();
    private List<Long> ruas = new ArrayList<>();
    private List<Long> tiposResiduos = new ArrayList<>();
    private double distanciaTotal;

    public RotaResponseDTO() {}

    public RotaResponseDTO(Rota rota) {
        if (rota == null) return;
        this.id = rota.getId();
        this.caminhaoId = rota.getCaminhao() != null ? rota.getCaminhao().getId() : null;
        if (rota.getBairros() != null) {
            for (Bairro b : rota.getBairros()) if (b != null && b.getId() != null) this.bairros.add(b.getId());
        }
        if (rota.getRuas() != null) {
            for (Rua r : rota.getRuas()) if (r != null && r.getId() != null) this.ruas.add(r.getId());
        }
        if (rota.getTiposResiduos() != null) {
            for (Residuo res : rota.getTiposResiduos()) if (res != null && res.getId() != null) this.tiposResiduos.add(res.getId());
        }
        this.distanciaTotal = rota.getDistanciaTotal();
    }

    // Getters / Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getCaminhaoId() { return caminhaoId; }
    public void setCaminhaoId(Long caminhaoId) { this.caminhaoId = caminhaoId; }

    public List<Long> getBairros() { return bairros; }
    public void setBairros(List<Long> bairros) { this.bairros = bairros; }

    public List<Long> getRuas() { return ruas; }
    public void setRuas(List<Long> ruas) { this.ruas = ruas; }

    public List<Long> getTiposResiduos() { return tiposResiduos; }
    public void setTiposResiduos(List<Long> tiposResiduos) { this.tiposResiduos = tiposResiduos; }

    public double getDistanciaTotal() { return distanciaTotal; }
    public void setDistanciaTotal(double distanciaTotal) { this.distanciaTotal = distanciaTotal; }
}

package com.obelix.pi.controllers.DTO;

import java.util.ArrayList;
import java.util.List;

import com.obelix.pi.model.Caminhao;
import com.obelix.pi.model.Residuo;

/**
 * DTO de resposta do caminhão — contém ids de resíduos.
 */
public class CaminhaoResponseDTO {
    private Long id;
    private String placa;
    private String motorista;
    private double capacidade;
    private List<Long> tiposResiduos = new ArrayList<>();

    public CaminhaoResponseDTO() {}

    public CaminhaoResponseDTO(Caminhao caminhao) {
        if (caminhao == null) return;
        this.id = caminhao.getId();
        this.placa = caminhao.getPlaca();
        this.motorista = caminhao.getMotorista();
        this.capacidade = caminhao.getCapacidade();
        if (caminhao.getTiposResiduos() != null) {
            for (Residuo r : caminhao.getTiposResiduos()) {
                if (r != null && r.getId() != null) this.tiposResiduos.add(r.getId());
            }
        }
    }

    // Getters e setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getPlaca() { return placa; }
    public void setPlaca(String placa) { this.placa = placa; }

    public String getMotorista() { return motorista; }
    public void setMotorista(String motorista) { this.motorista = motorista; }

    public double getCapacidade() { return capacidade; }
    public void setCapacidade(double capacidade) { this.capacidade = capacidade; }

    public List<Long> getTiposResiduos() { return tiposResiduos; }
    public void setTiposResiduos(List<Long> tiposResiduos) { this.tiposResiduos = tiposResiduos; }
}

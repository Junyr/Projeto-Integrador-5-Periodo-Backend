package com.obelix.pi.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "caminhao")
public class Caminhao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String placa;
    private String motorista;
    private double capacidade;

    @ManyToMany
    @JoinTable(
        name = "caminhao_tipos_residuos",
        joinColumns = @JoinColumn(name = "caminhao_id"),
        inverseJoinColumns = @JoinColumn(name = "tipos_residuos_id")
    )
    private List<Residuo> tiposResiduos = new ArrayList<>();

    public Caminhao() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getPlaca() { return placa; }
    public void setPlaca(String placa) { this.placa = placa; }

    public String getMotorista() { return motorista; }
    public void setMotorista(String motorista) { this.motorista = motorista; }

    public double getCapacidade() { return capacidade; }
    public void setCapacidade(double capacidade) { this.capacidade = capacidade; }

    public List<Residuo> getTiposResiduos() { return tiposResiduos; }
    public void setTiposResiduos(List<Residuo> tiposResiduos) { this.tiposResiduos = tiposResiduos; }
}

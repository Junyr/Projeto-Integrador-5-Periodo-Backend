package com.obelix.pi.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.FetchType;
import java.util.List;

@Entity
public class Caminhao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String placa;
    private String motorista;
    private double capacidade;

    @OneToMany(fetch = FetchType.LAZY)
    private List<Residuo> tiposResiduos;

    public Caminhao() {
    }

    public Caminhao(Long id, String placa, String motorista, double capacidade, List<Residuo> tiposResiduos) {
        this.id = id;
        this.placa = placa;
        this.motorista = motorista;
        this.capacidade = capacidade;
        this.tiposResiduos = tiposResiduos;
    }

    public Long getId() {
        return id;
    }

    public String getPlaca() {
        return placa;
    }

    public String getMotorista() {
        return motorista;
    }

    public double getCapacidade() {
        return capacidade;
    }

    public List<Residuo> getTiposResiduos() {
        return tiposResiduos;
    }
}


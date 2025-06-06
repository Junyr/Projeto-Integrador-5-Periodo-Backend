package com.obelix.pi.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.FetchType;

@Entity
public class Rua {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Bairro origem;

    @ManyToOne(fetch = FetchType.LAZY)
    private Bairro destino;

    private double distanciaKm;

    public Rua() {
    }

    public Rua(Long id, Bairro origem, Bairro destino, double distanciaKm) {
        this.id = id;
        this.origem = origem;
        this.destino = destino;
        this.distanciaKm = distanciaKm;
    }

    public Long getId() {
        return id;
    }

    public Bairro getOrigem() {
        return origem;
    }

    public Bairro getDestino() {
        return destino;
    }

    public double getDistanciaKm() {
        return distanciaKm;
    }
}


package com.obelix.pi.model;

import jakarta.persistence.*;

@Entity
@Table(name = "rua")
public class Rua {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "origem_id")
    private Bairro origem;

    @ManyToOne
    @JoinColumn(name = "destino_id")
    private Bairro destino;

    private double distanciaKm;

    public Rua() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Bairro getOrigem() { return origem; }
    public void setOrigem(Bairro origem) { this.origem = origem; }

    public Bairro getDestino() { return destino; }
    public void setDestino(Bairro destino) { this.destino = destino; }

    public double getDistanciaKm() { return distanciaKm; }
    public void setDistanciaKm(double distanciaKm) { this.distanciaKm = distanciaKm; }
}

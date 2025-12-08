package com.obelix.pi.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "itinerario")
public class Itinerario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate data;

    @ManyToOne
    @JoinColumn(name = "rota_id")
    private Rota rota;

    public Itinerario() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public LocalDate getData() { return data; }
    public void setData(LocalDate data) { this.data = data; }
    public Rota getRota() { return rota; }
    public void setRota(Rota rota) { this.rota = rota; }
}

package com.obelix.pi.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.FetchType;
import java.time.LocalDate;

@Entity
public class Itinerario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate data;

    @ManyToOne(fetch = FetchType.LAZY)
    private Rota rota;

    public Itinerario() {
    }

    public Itinerario(Long id, LocalDate data, Rota rota) {
        this.id = id;
        this.data = data;
        this.rota = rota;
    }

    public Long getId() {
        return id;
    }

    public LocalDate getData() {
        return data;
    }

    public Rota getRota() {
        return rota;
    }
}


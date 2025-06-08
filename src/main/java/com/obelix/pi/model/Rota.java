package com.obelix.pi.model;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "rota")
public class Rota {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Caminhao caminhao;

    @ManyToMany(fetch = FetchType.LAZY)
    private List<Bairro> bairros;

    @ManyToMany(fetch = FetchType.LAZY)
    private List<Rua> ruas;

    @ManyToMany(fetch = FetchType.LAZY)
    private List<Residuo> residuosAtendidos;

    private double distanciaTotal;
}

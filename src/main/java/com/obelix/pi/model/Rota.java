package com.obelix.pi.model;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;

@Entity
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

    public Rota() {
    }

    public Rota(Long id, Caminhao caminhao, List<Bairro> bairros, List<Rua> ruas, List<Residuo> residuosAtendidos, double distanciaTotal) {
        this.id = id;
        this.caminhao = caminhao;
        this.bairros = bairros;
        this.ruas = ruas;
        this.residuosAtendidos = residuosAtendidos;
        this.distanciaTotal = distanciaTotal;
    }

    public Long getId() {
        return id;
    }

    public Caminhao getCaminhao() {
        return caminhao;
    }

    public List<Bairro> getBairros() {
        return bairros;
    }

    public List<Rua> getRuas() {
        return ruas;
    }

    public List<Residuo> getResiduosAtendidos() {
        return residuosAtendidos;
    }

    public double getDistanciaTotal() {
        return distanciaTotal;
    }
    

}

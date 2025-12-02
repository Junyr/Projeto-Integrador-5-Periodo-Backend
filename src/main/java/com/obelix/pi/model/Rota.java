package com.obelix.pi.model;

import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.*;

@Entity
@Table(name = "rota")
public class Rota {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "caminhao_id")
    private Caminhao caminhao;

    @ManyToMany
    @JoinTable(
        name = "rota_bairros",
        joinColumns = @JoinColumn(name = "rota_id"),
        inverseJoinColumns = @JoinColumn(name = "bairro_id")
    )
    private List<Bairro> bairros = new ArrayList<>();

    @ManyToMany
    @JoinTable(
        name = "rota_ruas",
        joinColumns = @JoinColumn(name = "rota_id"),
        inverseJoinColumns = @JoinColumn(name = "rua_id")
    )
    private List<Rua> ruas = new ArrayList<>();

    @ManyToMany
    @JoinTable(
        name = "rota_tipos_residuos",
        joinColumns = @JoinColumn(name = "rota_id"),
        inverseJoinColumns = @JoinColumn(name = "tipo_residuo_id")
    )
    private List<Residuo> tiposResiduos = new ArrayList<>();

    private double distanciaTotal;

    public Rota() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Caminhao getCaminhao() { return caminhao; }
    public void setCaminhao(Caminhao caminhao) { this.caminhao = caminhao; }

    public List<Bairro> getBairros() { return bairros; }
    public void setBairros(List<Bairro> bairros) { this.bairros = bairros; }

    public List<Rua> getRuas() { return ruas; }
    public void setRuas(List<Rua> ruas) { this.ruas = ruas; }

    public List<Residuo> getTiposResiduos() { return tiposResiduos; }
    public void setTiposResiduos(List<Residuo> tiposResiduos) { this.tiposResiduos = tiposResiduos; }

    public double getDistanciaTotal() { return distanciaTotal; }
    public void setDistanciaTotal(double distanciaTotal) { this.distanciaTotal = distanciaTotal; }
}

package com.obelix.pi.model;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
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

    @ManyToOne
    @JoinColumn(name = "camihao_id")
    private Caminhao caminhao;

    @ManyToMany
    @JoinTable(
        name = "rota_bairros",
        joinColumns = @JoinColumn(name = "rota_id"),
        inverseJoinColumns = @JoinColumn(name = "bairro_id")
    )
    private List<Bairro> bairros;

    @ManyToMany
    @JoinTable(
        name = "rota_ruas",
        joinColumns = @JoinColumn(name = "rota_id"),
        inverseJoinColumns = @JoinColumn(name = "rua_id")
    )
    private List<Rua> ruas;

    @ManyToOne
    @JoinColumn(name = "tipo_residuo_id")
    private Residuo tipoResiduo;

    private double distanciaTotal;
}

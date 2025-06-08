package com.obelix.pi.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;
import lombok.Data;
import java.util.List;

@Data
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
    private List<Residuo> tiposResiduos;
}


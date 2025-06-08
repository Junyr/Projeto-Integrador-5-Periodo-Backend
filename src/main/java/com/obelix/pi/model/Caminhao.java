package com.obelix.pi.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import jakarta.persistence.FetchType;
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

    @OneToMany(fetch = FetchType.LAZY)
    private List<Residuo> tiposResiduos;
}


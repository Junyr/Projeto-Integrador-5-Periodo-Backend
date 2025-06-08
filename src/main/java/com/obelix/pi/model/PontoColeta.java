package com.obelix.pi.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import jakarta.persistence.FetchType;
import java.util.List;

@Data
@Entity
@Table(name = "ponto_coleta")
public class PontoColeta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String responsavel;
    private String telefoneResponsavel;
    private String emailResponsavel;
    private String endereco;
    private String horario;

    @ManyToOne
    @JoinColumn(name = "bairro_id")
    private Bairro bairro;

    @ManyToMany
    @JoinTable(
        name = "ponto_coleta_tipos_residuos",
        joinColumns = @JoinColumn(name = "ponto_coleta_id"),
        inverseJoinColumns = @JoinColumn(name = "tipos_residuos_id")
    )
    private List<Residuo> tiposResiduos;
}


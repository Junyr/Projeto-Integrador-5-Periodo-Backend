package com.obelix.pi.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.FetchType;
import java.util.List;

@Entity
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

    @ManyToOne(fetch = FetchType.LAZY)
    private Bairro bairro;

    @ManyToMany(fetch = FetchType.LAZY)
    private List<Residuo> tiposResiduos;

    public PontoColeta() {
    }

    public PontoColeta(Long id, String nome, String responsavel, String telefoneResponsavel,
                       String emailResponsavel, String endereco, String horario,
                       Bairro bairro, List<Residuo> tiposResiduos) {
        this.id = id;
        this.nome = nome;
        this.responsavel = responsavel;
        this.telefoneResponsavel = telefoneResponsavel;
        this.emailResponsavel = emailResponsavel;
        this.endereco = endereco;
        this.horario = horario;
        this.bairro = bairro;
        this.tiposResiduos = tiposResiduos;
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getResponsavel() {
        return responsavel;
    }

    public String getTelefoneResponsavel() {
        return telefoneResponsavel;
    }

    public String getEmailResponsavel() {
        return emailResponsavel;
    }

    public String getEndereco() {
        return endereco;
    }

    public String getHorario() {
        return horario;
    }

    public Bairro getBairro() {
        return bairro;
    }

    public List<Residuo> getTiposResiduos() {
        return tiposResiduos;
    }
}


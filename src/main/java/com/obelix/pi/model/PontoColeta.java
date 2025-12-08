package com.obelix.pi.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

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
    private List<Residuo> tiposResiduos = new ArrayList<>();

    public PontoColeta() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getResponsavel() { return responsavel; }
    public void setResponsavel(String responsavel) { this.responsavel = responsavel; }

    public String getTelefoneResponsavel() { return telefoneResponsavel; }
    public void setTelefoneResponsavel(String telefoneResponsavel) { this.telefoneResponsavel = telefoneResponsavel; }

    public String getEmailResponsavel() { return emailResponsavel; }
    public void setEmailResponsavel(String emailResponsavel) { this.emailResponsavel = emailResponsavel; }

    public String getEndereco() { return endereco; }
    public void setEndereco(String endereco) { this.endereco = endereco; }

    public String getHorario() { return horario; }
    public void setHorario(String horario) { this.horario = horario; }

    public Bairro getBairro() { return bairro; }
    public void setBairro(Bairro bairro) { this.bairro = bairro; }

    public List<Residuo> getTiposResiduos() { return tiposResiduos; }
    public void setTiposResiduos(List<Residuo> tiposResiduos) { this.tiposResiduos = tiposResiduos; }
}

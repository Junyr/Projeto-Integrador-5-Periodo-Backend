package com.obelix.pi.controllers.DTO;

import java.util.List;
import java.util.stream.Collectors;

import java.util.ArrayList;

import com.obelix.pi.model.PontoColeta;
import com.obelix.pi.model.Residuo;

/**
 * DTO de resposta para PontoColeta.
 */
public class PontoColetaResponseDTO {
    private Long id;
    private String nome;
    private String responsavel;
    private String telefoneResponsavel;
    private String emailResponsavel;
    private String endereco;
    private String horario;
    private Long bairroId;
    private List<Long> tiposResiduos = new ArrayList<>();

    public PontoColetaResponseDTO() {}

    public PontoColetaResponseDTO(PontoColeta pontoColeta) {
        if (pontoColeta == null) return;
        this.id = pontoColeta.getId();
        this.nome = pontoColeta.getNome();
        this.responsavel = pontoColeta.getResponsavel();
        this.telefoneResponsavel = pontoColeta.getTelefoneResponsavel();
        this.emailResponsavel = pontoColeta.getEmailResponsavel();
        this.endereco = pontoColeta.getEndereco();
        this.horario = pontoColeta.getHorario();
        this.bairroId = pontoColeta.getBairro() != null ? pontoColeta.getBairro().getId() : null;
        if (pontoColeta.getTiposResiduos() != null) {
            for (Residuo r : pontoColeta.getTiposResiduos()) {
                if (r != null && r.getId() != null) this.tiposResiduos.add(r.getId());
            }
        }
    }

    // Getters / Setters
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

    public Long getBairroId() { return bairroId; }
    public void setBairroId(Long bairroId) { this.bairroId = bairroId; }

    public List<Long> getTiposResiduos() { return tiposResiduos; }
    public void setTiposResiduos(List<Long> tiposResiduos) { this.tiposResiduos = tiposResiduos; }
}

package com.obelix.pi.controllers.DTO;

import java.util.ArrayList;
import java.util.List;

import com.obelix.pi.repository.BairroRepo;

/**
 * DTO de requisição para Ponto de Coleta.
 * Sem Lombok; getters/setters manuais.
 */
public class PontoColetaRequestDTO {

    private String nome;
    private String responsavel;
    private String telefoneResponsavel;
    private String emailResponsavel;
    private String endereco;
    private String horario;
    private Long bairroId;
    private List<Long> tiposResiduos = new ArrayList<>();

    public PontoColetaRequestDTO() {}

    // Getters / Setters
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
    public void setTiposResiduos(List<Long> tiposResiduos) {
        this.tiposResiduos = tiposResiduos == null ? new ArrayList<>() : tiposResiduos;
    }

    /**
     * Validação dos campos com uso do bairroRepo para garantir existência do bairro.
     */
    public boolean validarAtributos(BairroRepo bairroRepo) {
        if (this.nome == null || this.responsavel == null || this.telefoneResponsavel == null ||
            this.emailResponsavel == null || this.endereco == null || this.horario == null ||
            this.bairroId == null || this.tiposResiduos == null) {
            throw new RuntimeException("Por favor preencha todos os campos.");
        }
        if (this.nome.isBlank() || this.responsavel.isBlank() || this.telefoneResponsavel.isBlank() ||
            this.emailResponsavel.isBlank() || this.endereco.isBlank() || this.horario.isBlank() ||
            this.tiposResiduos.isEmpty()) {
            throw new RuntimeException("Por favor preencha todos os campos.");
        }
        if (!this.nome.matches("^[a-zA-Zà-úÀ-Úâ-ûÂ-ÛçÇ0-9]+(\\s[a-zA-Zà-úÀ-Úâ-ûÂ-ÛçÇ0-9]+)*$")) {
            throw new RuntimeException("Forneça um nome válido, apenas letras, números e espaços são permitidos.");
        }
        if (!this.responsavel.matches("^[a-zA-Zà-úÀ-Úâ-ûÂ-ÛçÇ]+(\\s[a-zA-Zà-úÀ-Úâ-ûÂ-ÛçÇ]+)*$")) {
            throw new RuntimeException("Forneça um nome válido para o responsável, apenas letras e espaços são permitidos.");
        }
        if (!this.telefoneResponsavel.matches("^\\(\\d{2}\\) \\d{4,5}-\\d{4}$")) {
            throw new RuntimeException("Forneça um telefone válido no formato (99) 99999-9999.");
        }
        if (!this.emailResponsavel.matches("^.+@(gmail\\.com|hotmail\\.com|outlook\\.(com|com\\.br)|ecoville\\.gov)$")) {
            throw new RuntimeException("Por favor, forneça um email válido.");
        }
        if (!bairroRepo.existsById(this.bairroId)) {
            throw new RuntimeException("Bairro não encontrado.");
        }
        if (this.tiposResiduos.size() < 1) {
            throw new RuntimeException("Informe ao menos um tipo de resíduo.");
        }
        return true;
    }
}

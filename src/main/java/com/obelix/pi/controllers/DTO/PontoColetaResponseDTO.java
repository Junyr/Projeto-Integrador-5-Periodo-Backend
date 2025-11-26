package com.obelix.pi.controllers.DTO;

import java.util.List;
import java.util.stream.Collectors;

import lombok.Data;

@Data
public class PontoColetaResponseDTO {
    private Long id;
    private String nome;
    private String responsavel;
    private String telefoneResponsavel;
    private String emailResponsavel;
    private String endereco;
    private String horario;
    private Long bairroId;
    private List<Long> tiposResiduos;

    public PontoColetaResponseDTO(com.obelix.pi.model.PontoColeta pontoColeta) {
        this.id = pontoColeta.getId();
        this.nome = pontoColeta.getNome();
        this.responsavel = pontoColeta.getResponsavel();
        this.telefoneResponsavel = pontoColeta.getTelefoneResponsavel();
        this.emailResponsavel = pontoColeta.getEmailResponsavel();
        this.endereco = pontoColeta.getEndereco();
        this.horario = pontoColeta.getHorario();
        this.bairroId = pontoColeta.getBairro() != null ? pontoColeta.getBairro().getId() : null;
        this.tiposResiduos = pontoColeta.getTiposResiduos()
            .stream()
            .map(residuo -> residuo.getId())
            .collect(Collectors.toList());
    }
}

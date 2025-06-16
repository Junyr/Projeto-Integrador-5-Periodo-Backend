package com.obelix.pi.controllers.DTO;

import java.util.List;

import com.obelix.pi.repository.BairroRepo;

import lombok.Data;

@Data
public class PontoColetaRequestDTO {
    private String nome;
    private String responsavel;
    private String telefoneResponsavel;
    private String emailResponsavel;
    private String endereco;
    private String horario;
    private Long bairroId;
    private List<Long> tiposResiduos;

    public boolean validarAtributos(BairroRepo bairroRepo){
        if(this.nome == null || this.responsavel == null || this.telefoneResponsavel == null || this.emailResponsavel == null || this.endereco == null || this.horario == null || this.bairroId == null || this.tiposResiduos == null ||
         this.nome.isEmpty() || this.responsavel.isEmpty() || this.telefoneResponsavel.isEmpty() || this.emailResponsavel.isEmpty() || this.endereco.isEmpty() || this.horario.isEmpty() || this.tiposResiduos.isEmpty()) {
            throw new RuntimeException("Por favor preencha todos os campos.");
        }

        if(this.nome.matches("^[a-zA-Zà-úÀ-Úâ-ûÂ-ÛçÇ0-9]+(\\s[a-zA-Zà-úÀ-Úâ-ûÂ-ÛçÇ0-9]+)*$")){
            if(this.responsavel.matches("^[a-zA-Zà-úÀ-Úâ-ûÂ-ÛçÇ]+(\\s[a-zA-Zà-úÀ-Úâ-ûÂ-ÛçÇ]+)?$")){
                if(this.telefoneResponsavel.matches("^\\(\\d{2}\\) \\d{4,5}-\\d{4}$")){
                    if(this.emailResponsavel.matches("^.+@(gmail\\.com|hotmail\\.com|outlook\\.(com|com\\.br)|ecoville\\.gov)$")){
                        if(this.endereco.matches("^.+$")){
                            if(this.horario.matches("^.+$")){
                                if(bairroRepo.existsById(bairroId)){
                                    if(this.tiposResiduos.size() >= 1){
                                        return true;
                                    } else throw new RuntimeException("Informe ao menos um tipo de resíduo.");
                                } else throw new RuntimeException("Bairro não encontrado.");
                            } else throw new RuntimeException("Forneça um horário válido.");
                        } else throw new RuntimeException("Forneça um endereço válido.");
                    } else throw new RuntimeException("Por favor, forneça um email válido.");
                } else throw new RuntimeException("Forneça um telefone válido no formato (99) 99999-9999.");
            } else throw new RuntimeException("Forneça um nome válido para o responsável, apenas letras e espaços são permitidos.");
        } else throw new RuntimeException("Forneça um nome válido, apenas letras e espaços são permitidos.");
    }
}

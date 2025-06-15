package com.obelix.pi.controllers.DTO;

import java.util.List;

import lombok.Data;

@Data
public class CaminhaoRequestDTO {
    private String placa;
    private String motorista;
    private double capacidade;
    private List<Long> tiposResiduos;

    public boolean validarAtributos(){
        if(this.placa == null || this.motorista == null || this.capacidade == 0 || this.tiposResiduos == null ||
         this.placa.isEmpty() || this.motorista.isEmpty() || this.tiposResiduos.isEmpty()) {
            throw new RuntimeException("Por favor preencha todos os campos obrigatórios: Placa, Motorista, Capacidade e Tipos de residuos.");
        }

        if(this.placa.matches("^[A-Z]{3}[0-9][A-Z][0-9]{2}$")){
            if(this.motorista.matches("^[a-zA-Zà-úÀ-Úâ-ûÂ-ÛçÇ]+(\\s[a-zA-Zà-úÀ-Úâ-ûÂ-ÛçÇ]+)?$")){
                if(this.capacidade > 0){
                    if(this.tiposResiduos.size() >= 1){
                        return true;
                    } else throw new RuntimeException("Informe os tipos de residuos.");
                } else throw new RuntimeException("Forneça um numero válido para a capacidade.");
            } else throw new RuntimeException("Forneça um nome válido, apenas letras e espaços são permitidos.");
        } else throw new RuntimeException("Numero de placa invalida, use o modelo Mercosul (ABC1D23)!");
    }

}

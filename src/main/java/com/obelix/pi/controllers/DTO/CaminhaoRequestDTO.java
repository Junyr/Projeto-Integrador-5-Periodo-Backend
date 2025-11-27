package com.obelix.pi.controllers.DTO;

import java.util.ArrayList;
import java.util.List;

/**
 * DTO de requisição para criação/atualização de Caminhão.
 * Mantém método de validação similar ao seu original, mas mais robusto em mensagens.
 */
public class CaminhaoRequestDTO {
    private String placa;
    private String motorista;
    private double capacidade;
    private List<Long> tiposResiduos = new ArrayList<>();

    // Getters e Setters
    public String getPlaca() { return placa; }
    public void setPlaca(String placa) { this.placa = placa; }

    public String getMotorista() { return motorista; }
    public void setMotorista(String motorista) { this.motorista = motorista; }

    public double getCapacidade() { return capacidade; }
    public void setCapacidade(double capacidade) { this.capacidade = capacidade; }

    public List<Long> getTiposResiduos() { return tiposResiduos; }
    public void setTiposResiduos(List<Long> tiposResiduos) {
        this.tiposResiduos = tiposResiduos == null ? new ArrayList<>() : tiposResiduos;
    }

    /**
     * Valida atributos do DTO. Lança RuntimeException com mensagem específica caso inválido.
     */
    public boolean validarAtributos() {
        if (this.placa == null || this.motorista == null || this.tiposResiduos == null) {
            throw new RuntimeException("Por favor preencha todos os campos obrigatórios: Placa, Motorista, Capacidade e Tipos de resíduos.");
        }
        if (this.placa.isEmpty() || this.motorista.isEmpty() || this.tiposResiduos.isEmpty()) {
            throw new RuntimeException("Por favor preencha todos os campos obrigatórios: Placa, Motorista, Capacidade e Tipos de resíduos.");
        }
        // Valida placa no padrão MERCOSUL (exemplo ABC1D23)
        if (!this.placa.matches("^[A-Z]{3}[0-9][A-Z][0-9]{2}$")) {
            throw new RuntimeException("Número de placa inválida, use o modelo Mercosul (ABC1D23).");
        }
        // Valida nome do motorista (apenas letras e espaços, acentuação permitida)
        if (!this.motorista.matches("^[a-zA-Zà-úÀ-Úâ-ûÂ-ÛçÇ]+(\\s[a-zA-Zà-úÀ-Úâ-ûÂ-ÛçÇ]+)*$")) {
            throw new RuntimeException("Forneça um nome válido para o motorista, apenas letras e espaços são permitidos.");
        }
        if (this.capacidade <= 0) {
            throw new RuntimeException("Forneça um número válido para a capacidade.");
        }
        if (this.tiposResiduos.size() < 1) {
            throw new RuntimeException("Informe ao menos um tipo de resíduo.");
        }
        return true;
    }
}

package com.obelix.pi.validation;

import com.obelix.pi.exception.BadRequestException;

public class ValidadorEntrada {

    public static void requireValidPlaca(String placa) {
        if (!ValidadorPlacaVeiculo.isValid(placa)) {
            throw new BadRequestException("Placa inválida: " + placa);
        }
    }

    public static void requireValidCpf(String cpf) {
        if (!ValidadorCpf.isValid(cpf)) {
            throw new BadRequestException("CPF inválido: " + cpf);
        }
    }

    public static void requireValidCnpj(String cnpj) {
        if (!ValidadorCnpj.isValid(cnpj)) {
            throw new BadRequestException("CNPJ inválido: " + cnpj);
        }
    }
}

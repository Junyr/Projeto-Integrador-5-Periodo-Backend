package com.obelix.pi.service;


import org.springframework.stereotype.Service;

import com.obelix.pi.model.Bairro;
import com.obelix.pi.model.Rota;
import com.obelix.pi.service.interfaces.IRotaService;

@Service
public class RotaService implements IRotaService {

    @Override
    public Rota gerarRota(Bairro origem, Bairro destino) {
        // TODO: implementar
        return null;
    }

    @Override
    public boolean validarOtimizacaoRota() {
        // TODO: implementar
        return false;
    }
}
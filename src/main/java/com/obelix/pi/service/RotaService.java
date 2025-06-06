package com.obelix.pi.service;

import java.util.List;

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
    public Rota buscarRota(Bairro origem, Bairro destino) {
        // TODO: implementar
        return null;
    }

    @Override
    public void associarCaminhaoRota(Long caminhaoId, Rota rotaOtimizada) {
        // TODO: implementar
    }

    @Override
    public boolean salvarRota(Rota rota) {
        // TODO: implementar
        return false;
    }

    @Override
    public List<Rota> listarRota() {
        // TODO: implementar
        return null;
    }

    @Override
    public boolean validarOtimizacaoRota() {
        // TODO: implementar
        return false;
    }
}
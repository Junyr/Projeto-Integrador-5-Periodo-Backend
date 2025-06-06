package com.obelix.pi.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.obelix.pi.model.PontoColeta;
import com.obelix.pi.service.interfaces.IPontoColetaService;

@Service
public class PontoColetaService implements IPontoColetaService {

    @Override
    public void cadastrarPontoColeta(PontoColeta pontoColeta) {
        // TODO: implementar
    }

    @Override
    public void atualizarPontoColeta(Long id, PontoColeta pontoColeta) {
        // TODO: implementar
    }

    @Override
    public List<PontoColeta> listarPontoColeta() {
        // TODO: implementar
        return null;
    }

    @Override
    public List<PontoColeta> listarPontoColetaPorTipoResiduo() {
        // TODO: implementar
        return null;
    }

    @Override
    public PontoColeta buscarPontoColeta(Long id, String nome) {
        // TODO: implementar
        return null;
    }

    @Override
    public void deletarPontoColeta(Long id) {
        // TODO: implementar
    }
}


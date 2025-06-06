package com.obelix.pi.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.obelix.pi.model.Caminhao;
import com.obelix.pi.model.Residuo;
import com.obelix.pi.service.interfaces.ICaminhaoService;

@Service
public class CaminhaoService implements ICaminhaoService {

    @Override
    public void cadastrarCaminhao(Caminhao caminhao) {
        // TODO: implementar
    }

    @Override
    public void atualizarCaminhao(Long id, Caminhao caminhao) {
        // TODO: implementar
    }

    @Override
    public List<Caminhao> listarCaminhao() {
        // TODO: implementar
        return null;
    }

    @Override
    public List<Caminhao> listarCaminhaoPorTipoResiduos(String tipoResiduo) {
        // TODO: implementar
        return null;
    }

    @Override
    public Caminhao buscarCaminhao(Long id, String placa) {
        // TODO: implementar
        return null;
    }

    @Override
    public void deletarCaminhao(Long id) {
        // TODO: implementar
    }

    @Override
    public boolean verificarDisponibilidade(Long id, LocalDate data) {
        // TODO: implementar
        return false;
    }

    @Override
    public boolean validarCompatibilidadeComResiduos(Long id, List<Residuo> residuos) {
        // TODO: implementar
        return false;
    }
}

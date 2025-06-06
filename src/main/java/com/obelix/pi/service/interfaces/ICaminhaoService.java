package com.obelix.pi.service.interfaces;

import java.time.LocalDate;
import java.util.List;

import com.obelix.pi.model.Caminhao;
import com.obelix.pi.model.Residuo;

public interface ICaminhaoService {

    void cadastrarCaminhao(Caminhao caminhao);
    void atualizarCaminhao(Long id, Caminhao caminhao);
    List<Caminhao> listarCaminhao();
    List<Caminhao> listarCaminhaoPorTipoResiduos(String tipoResiduo);
    Caminhao buscarCaminhao(Long id, String placa);
    void deletarCaminhao(Long id);
    boolean verificarDisponibilidade(Long id, LocalDate data);
    boolean validarCompatibilidadeComResiduos(Long id, List<Residuo> residuos);

}

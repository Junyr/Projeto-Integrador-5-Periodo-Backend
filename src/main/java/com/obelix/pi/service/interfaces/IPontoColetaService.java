package com.obelix.pi.service.interfaces;

import java.util.List;

import com.obelix.pi.model.PontoColeta;

public interface IPontoColetaService {
    
    void cadastrarPontoColeta(PontoColeta pontoColeta);
    void atualizarPontoColeta(Long id, PontoColeta pontoColeta);
    List<PontoColeta> listarPontoColeta();
    List<PontoColeta> listarPontoColetaPorTipoResiduo();
    PontoColeta buscarPontoColeta(Long id, String nome);
    void deletarPontoColeta(Long id);
    
}

package com.obelix.pi.service.interfaces;

import java.util.List;

import com.obelix.pi.model.Bairro;
import com.obelix.pi.model.Rota;

public interface IRotaService {
    
    Rota gerarRota(Bairro origem, Bairro destino);
    Rota buscarRota(Bairro origem, Bairro destino);
    void associarCaminhaoRota(Long caminhao, Rota rotaOtimizada);
    boolean salvarRota(Rota rota);
    List<Rota> listarRota();
    boolean validarOtimizacaoRota();

}

package com.obelix.pi.service.interfaces;

import com.obelix.pi.model.Bairro;
import com.obelix.pi.model.Rota;

public interface IRotaService {
    
    Rota gerarRota(Bairro origem, Bairro destino);
    boolean validarOtimizacaoRota();

}

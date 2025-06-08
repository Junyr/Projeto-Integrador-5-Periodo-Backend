package com.obelix.pi.service.interfaces;

import com.obelix.pi.model.Residuo;
import com.obelix.pi.model.Rota;

public interface IRotaService {
    
    Rota gerarRota(Long caminhaoId, Long pontoColetaOrigemId, Long pontoColetaDestinoId, Residuo residuo);
    boolean atualizarRota(Rota rota);

}

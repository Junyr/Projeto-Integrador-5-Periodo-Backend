package com.obelix.pi.service.interfaces;

import com.obelix.pi.controllers.DTO.RotaRequestDTO;
import com.obelix.pi.model.Rota;

public interface IRotaService {
    
    Rota gerarRota(RotaRequestDTO requestDTO);
    void atualizarRotas();

}

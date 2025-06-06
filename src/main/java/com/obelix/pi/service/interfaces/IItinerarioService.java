package com.obelix.pi.service.interfaces;

import java.util.Date;
import java.util.List;

import com.obelix.pi.model.Itinerario;
import com.obelix.pi.model.Rota;

public interface IItinerarioService {

    void associarRotaDiaEspecifico(Rota rota, Date dia);
    Itinerario gerarItinerarioDiario();
    boolean validarRota(Rota rota);
    List<Itinerario> listarItinerarioMensal();

}

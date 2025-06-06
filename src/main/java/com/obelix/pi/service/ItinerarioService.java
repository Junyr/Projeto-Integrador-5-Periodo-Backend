package com.obelix.pi.service;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.obelix.pi.model.Itinerario;
import com.obelix.pi.model.Rota;
import com.obelix.pi.service.interfaces.IItinerarioService;

@Service
public class ItinerarioService implements IItinerarioService {

    @Override
    public void associarRotaDiaEspecifico(Rota rota, Date dia) {
        // TODO: implementar
    }

    @Override
    public Itinerario gerarItinerarioDiario() {
        // TODO: implementar
        return null;
    }

    @Override
    public boolean validarRota(Rota rota) {
        // TODO: implementar
        return false;
    }

    @Override
    public List<Itinerario> listarItinerarioMensal() {
        // TODO: implementar
        return null;
    }
}


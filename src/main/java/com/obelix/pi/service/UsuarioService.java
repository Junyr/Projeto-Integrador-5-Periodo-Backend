package com.obelix.pi.service;

import java.util.Date;

import org.springframework.stereotype.Service;

import com.obelix.pi.model.Itinerario;
import com.obelix.pi.model.Rota;
import com.obelix.pi.service.interfaces.IUsuarioService;

@Service
public class UsuarioService implements IUsuarioService {

    @Override
    public void gerarSenhaHash(Rota rota, Date dia) {
        // TODO: implementar
    }

    @Override
    public boolean validarUsuario(Itinerario itinerario) {
        // TODO: implementar
        return false;
    }
}


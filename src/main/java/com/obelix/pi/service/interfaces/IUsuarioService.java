package com.obelix.pi.service.interfaces;

import java.util.Date;

import com.obelix.pi.model.Itinerario;
import com.obelix.pi.model.Rota;

public interface IUsuarioService {
    
    void gerarSenhaHash(Rota rota, Date dia);
    boolean validarUsuario(Itinerario itinerario);

}

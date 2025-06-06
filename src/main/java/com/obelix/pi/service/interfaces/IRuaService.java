package com.obelix.pi.service.interfaces;

import com.obelix.pi.model.Rua;

public interface IRuaService {
    
    void cadastrarRua(Rua rua);
    void atualizarRua(Rua rua);
    void deletarRua(Long idRua);

}

package com.obelix.pi.service.interfaces;

import com.obelix.pi.model.Bairro;

public interface IBairroService {

    void cadastrarBairro(Bairro bairro);
    void atualizarBairro(Bairro bairro);
    void deletarBairro(Long idBairro);
    
}

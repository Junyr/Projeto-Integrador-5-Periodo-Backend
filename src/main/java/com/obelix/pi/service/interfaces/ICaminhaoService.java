package com.obelix.pi.service.interfaces;

import java.time.LocalDate;

public interface ICaminhaoService {

    boolean verificarDisponibilidade(Long id, LocalDate data);
    boolean validarCompatibilidadeComResiduos(Long rotaId, Long id);

}

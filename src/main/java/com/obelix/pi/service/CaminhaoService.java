package com.obelix.pi.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.obelix.pi.model.Residuo;
import com.obelix.pi.repository.CaminhaoRepo;
import com.obelix.pi.repository.PontoColetaRepo;
import com.obelix.pi.service.interfaces.ICaminhaoService;

@Service
public class CaminhaoService implements ICaminhaoService {

    @Autowired
    CaminhaoRepo caminhaoRepo;

    @Autowired
    PontoColetaRepo pontoColetaRepo;

    // Padrão Strategy: Diferentes estratégias de validação de resíduos
    public interface ValidacaoResiduosStrategy {
        boolean validar(Long caminhaoId, Long pontoColetaId);
    }

    // Estratégia concreta
    public class ValidacaoCompatibilidade implements ValidacaoResiduosStrategy {
        @Override
        public boolean validar(Long caminhaoId, Long pontoColetaId) {
            if(caminhaoRepo.existsById(caminhaoId) && pontoColetaRepo.existsById(pontoColetaId)){
                List<Residuo> caminhaoResiduos = caminhaoRepo.getReferenceById(caminhaoId).getTiposResiduos();
                List<Residuo> pontoColetaResiduos = pontoColetaRepo.getReferenceById(pontoColetaId).getTiposResiduos();
                return caminhaoResiduos.stream().anyMatch(pontoColetaResiduos::contains);
            }
            return false;
        }
    }

    private ValidacaoResiduosStrategy strategy = new ValidacaoCompatibilidade();

    @Override
    public boolean validarCompatibilidadeComResiduos(Long caminhaoId, Long pontoColetaId) {
        return strategy.validar(caminhaoId, pontoColetaId);
    }
}

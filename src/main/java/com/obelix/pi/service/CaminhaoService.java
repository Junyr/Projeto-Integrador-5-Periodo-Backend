package com.obelix.pi.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.obelix.pi.model.Residuo;
import com.obelix.pi.repository.CaminhaoRepo;
import com.obelix.pi.repository.PontoColetaRepo;
import com.obelix.pi.service.interfaces.ICaminhaoService;

@Service
public class CaminhaoService implements ICaminhaoService {

    @Autowired
    private CaminhaoRepo caminhaoRepo;

    @Autowired
    private PontoColetaRepo pontoColetaRepo;

    // Strategy pattern internal
    public interface ValidacaoResiduosStrategy {
        boolean validar(Long caminhaoId, Long pontoColetaId);
    }

    public class ValidacaoCompatibilidade implements ValidacaoResiduosStrategy {
        @Override
        public boolean validar(Long caminhaoId, Long pontoColetaId) {
            if (caminhaoId == null || pontoColetaId == null)
                throw new IllegalArgumentException("IDs não podem ser nulos");

            if (!caminhaoRepo.existsById(caminhaoId) ||
                    !pontoColetaRepo.existsById(pontoColetaId))
                return false;

            List<Residuo> residuosCaminhao = caminhaoRepo.findById(caminhaoId)
                    .map(c -> Optional.ofNullable(c.getTiposResiduos()).orElse(List.of()))
                    .orElse(List.of());

            List<Residuo> residuosPonto = pontoColetaRepo.findById(pontoColetaId)
                    .map(p -> Optional.ofNullable(p.getTiposResiduos()).orElse(List.of()))
                    .orElse(List.of());

            return residuosCaminhao.stream().anyMatch(residuosPonto::contains);
        }
    }


    private final ValidacaoResiduosStrategy strategy = new ValidacaoCompatibilidade();

    @Override
    public boolean validarCompatibilidadeComResiduos(Long caminhaoId, Long pontoColetaId) {
        return strategy.validar(caminhaoId, pontoColetaId);
    }

    @Override
    public boolean verificarDisponibilidade(Long caminhaoId, LocalDate data) {
        if (caminhaoId == null || data == null) {
            throw new IllegalArgumentException("ID do caminhão e data não podem ser nulos");
        }
        // Implementação simples: apenas checa existência. Ajuste conforme sua lógica real (itinerários agendados).
        return caminhaoRepo.existsById(caminhaoId);
    }
}

package com.obelix.pi.service;

import java.time.LocalDate;
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
    private CaminhaoRepo caminhaoRepo;

    @Autowired
    private PontoColetaRepo pontoColetaRepo;

    // Padrão Strategy: Diferentes estratégias de validação de resíduos
    public interface ValidacaoResiduosStrategy {
        boolean validar(Long caminhaoId, Long pontoColetaId);
    }

    // Estratégia concreta de validação de compatibilidade
    public class ValidacaoCompatibilidade implements ValidacaoResiduosStrategy {
        @Override
        public boolean validar(Long caminhaoId, Long pontoColetaId) {
            // Verificar se os IDs não são nulos
            if (caminhaoId == null || pontoColetaId == null) {
                throw new IllegalArgumentException("IDs não podem ser nulos");
            }

            if(caminhaoRepo.existsById(caminhaoId) && pontoColetaRepo.existsById(pontoColetaId)) {
                List<Residuo> caminhaoResiduos = caminhaoRepo.getReferenceById(caminhaoId).getTiposResiduos();
                List<Residuo> pontoColetaResiduos = pontoColetaRepo.getReferenceById(pontoColetaId).getTiposResiduos();
                return caminhaoResiduos.stream().anyMatch(pontoColetaResiduos::contains);
            }
            return false;
        }
    }

    // Campo final para estratégia
    private final ValidacaoResiduosStrategy strategy = new ValidacaoCompatibilidade();

    // Método para validar compatibilidade de resíduos
    @Override
    public boolean validarCompatibilidadeComResiduos(Long caminhaoId, Long pontoColetaId) {
        return strategy.validar(caminhaoId, pontoColetaId);
    }

    // Implementação do método verificarDisponibilidade da interface ICaminhaoService
    @Override
    public boolean verificarDisponibilidade(Long caminhaoId, LocalDate data) {
        if (caminhaoId == null || data == null) {
            throw new IllegalArgumentException("ID do caminhão e data não podem ser nulos");
        }

        // Lógica para verificar a disponibilidade do caminhão
        // Aqui você pode implementar a lógica para verificar se o caminhão está disponível na data informada
        return caminhaoRepo.existsById(caminhaoId); // A lógica real deve ser mais detalhada, como verificar se o caminhão já está agendado para essa data.
    }
}

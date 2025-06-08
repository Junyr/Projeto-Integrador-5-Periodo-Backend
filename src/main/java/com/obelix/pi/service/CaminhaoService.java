package com.obelix.pi.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.obelix.pi.model.Residuo;
import com.obelix.pi.repository.CaminhaoRepo;
import com.obelix.pi.repository.ItinerarioRepo;
import com.obelix.pi.repository.RotaRepo;
import com.obelix.pi.service.interfaces.ICaminhaoService;

@Service
public class CaminhaoService implements ICaminhaoService {

    @Autowired
    CaminhaoRepo caminhaoRepo;

    @Autowired
    ItinerarioRepo itinerarioRepo;

    @Autowired
    RotaRepo rotaRepo;

    @Override
    public boolean verificarDisponibilidade(Long caminhaoId, LocalDate data) {
        if(caminhaoRepo.existsById(caminhaoId))
            return !itinerarioRepo.existsByDataAndRota_Caminhao_Id(data, caminhaoId);
        return false;
    }

    @Override
    public boolean validarCompatibilidadeComResiduos(Long caminhaoId, Long rotaId) {
        if(caminhaoRepo.existsById(caminhaoId) && rotaRepo.existsById(rotaId)){
            List<Residuo> caminhaoResiduos = caminhaoRepo.getReferenceById(caminhaoId).getTiposResiduos();
            List<Residuo> rotaResiduos = rotaRepo.getReferenceById(rotaId).getResiduosAtendidos();
            return rotaResiduos.stream().anyMatch(caminhaoResiduos::contains);
        }
        return false;
    }
}

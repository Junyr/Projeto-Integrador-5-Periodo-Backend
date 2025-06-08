package com.obelix.pi.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import com.obelix.pi.model.Bairro;
import com.obelix.pi.model.Residuo;
import com.obelix.pi.model.Rota;
import com.obelix.pi.model.Rua;
import com.obelix.pi.repository.CaminhaoRepo;
import com.obelix.pi.repository.PontoColetaRepo;
import com.obelix.pi.repository.RotaRepo;
import com.obelix.pi.service.interfaces.IRotaService;

@Service
public class RotaService implements IRotaService {

    @Autowired
    private CaminhaoService caminhaoService;

    @Autowired
    private DijkstraService dijkstraService;

    @Autowired
    private CaminhaoRepo caminhaoRepo;

    @Autowired
    private PontoColetaRepo pontoColetaRepo;

    @Autowired
    private RotaRepo rotaRepo;

    @Override
    public Rota gerarRota(Long caminhaoId, Long pontoColetaOrigemId, Long pontoColetaDestinoId, Residuo residuo) {
        if(caminhaoRepo.existsById(caminhaoId) &&
           pontoColetaRepo.existsById(pontoColetaOrigemId) &&
           pontoColetaRepo.existsById(pontoColetaDestinoId) &&
           caminhaoService.validarCompatibilidadeComResiduos(caminhaoId, pontoColetaOrigemId) &&
           caminhaoRepo.getReferenceById(caminhaoId).getTiposResiduos().contains(residuo)) {
            
            Rota rota = new Rota();
            rota.setCaminhao(caminhaoRepo.getReferenceById(caminhaoId));

            List<Rua> caminho = dijkstraService.encontrarCaminhoMaisCurto(pontoColetaOrigemId, pontoColetaDestinoId);
            List<Bairro> bairroCaminho = new ArrayList<>();
            if(caminho != null){
                for(Rua rua : caminho) {
                    bairroCaminho.add(rua.getOrigem());
                    rota.setDistanciaTotal(rota.getDistanciaTotal() + rua.getDistanciaKm());
                }
            }

            rota.setBairros(bairroCaminho);
            rota.setRuas(caminho);
            rota.setTipoResiduo(residuo);
            rotaRepo.save(rota);
            return rota;
           }
        throw new RuntimeException("Dados inválidos para gerar rota.");
    }

    @Override
    public boolean atualizarRota(Rota rota) {
        // TODO: implementar
        return false;
    }
}
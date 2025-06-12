package com.obelix.pi.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import com.obelix.pi.controllers.DTO.RotaRequestDTO;
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
    public Rota gerarRota(RotaRequestDTO requestDTO) {
        if(caminhaoRepo.existsById(requestDTO.getCaminhaoId())) {
            if(pontoColetaRepo.existsById(requestDTO.getPontoColetaOrigemId())) {
                if(pontoColetaRepo.existsById(requestDTO.getPontoColetaDestinoId())) {
                    if(caminhaoService.validarCompatibilidadeComResiduos(requestDTO.getCaminhaoId(), requestDTO.getPontoColetaDestinoId()) &&
                        caminhaoService.validarCompatibilidadeComResiduos(requestDTO.getCaminhaoId(), requestDTO.getPontoColetaOrigemId())) {
                            Rota rota = new Rota();
                            rota.setCaminhao(caminhaoRepo.getReferenceById(requestDTO.getCaminhaoId()));

                            List<Rua> caminho = dijkstraService.encontrarCaminhoMaisCurto(requestDTO.getPontoColetaOrigemId(), requestDTO.getPontoColetaDestinoId());
                            List<Bairro> bairroCaminho = new ArrayList<>();
                            if(caminho != null){
                                for(Rua rua : caminho) {
                                    bairroCaminho.add(rua.getOrigem());
                                    rota.setDistanciaTotal(rota.getDistanciaTotal() + rua.getDistanciaKm());
                                }
                            }

                            rota.setBairros(bairroCaminho);
                            rota.setRuas(caminho);
                            rota.setTipoResiduo(requestDTO.getTipoResiduo());
                            rotaRepo.save(rota);
                            return rota;
                    } throw new RuntimeException("Erro ao gerar rota: Tipo de residuo não compativel.");
                } throw new RuntimeException("Erro ao gerar rota: Ponto de Coleta de destino não existe.");
           } throw new RuntimeException("Erro ao gerar rota: Ponto de Coleta de origem não existe.");
        } throw new RuntimeException("Erro ao gerar rota: Caminhão não existe.");
    }

    @Override
    public void atualizarRotas() {
        List<Rota> rotas = rotaRepo.findAll();
        for (Rota rota : rotas) {
            Long caminhaoId = rota.getCaminhao().getId();
            Long pontoColetaOrigemId = rota.getRuas().get(0).getOrigem().getId();
            Long pontoColetaDestinoId = rota.getRuas().get(rota.getRuas().size() - 1).getDestino().getId();
            Residuo tipoResiduo = rota.getTipoResiduo();

            RotaRequestDTO requestDTO = new RotaRequestDTO();
            requestDTO.setCaminhaoId(caminhaoId);
            requestDTO.setPontoColetaOrigemId(pontoColetaOrigemId);
            requestDTO.setPontoColetaDestinoId(pontoColetaDestinoId);
            requestDTO.setTipoResiduo(tipoResiduo);

            Rota rotaOtimizada = gerarRota(requestDTO);
            rota.setBairros(rotaOtimizada.getBairros());
            rota.setRuas(rotaOtimizada.getRuas());
            rota.setDistanciaTotal(rotaOtimizada.getDistanciaTotal());
            rotaRepo.save(rota);
        }
    }
}
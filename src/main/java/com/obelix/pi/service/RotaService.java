package com.obelix.pi.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import com.obelix.pi.controllers.DTO.RotaRequestDTO;
import com.obelix.pi.model.Bairro;
import com.obelix.pi.model.PontoColeta;
import com.obelix.pi.model.Residuo;
import com.obelix.pi.model.Rota;
import com.obelix.pi.model.Rua;
import com.obelix.pi.repository.BairroRepo;
import com.obelix.pi.repository.CaminhaoRepo;
import com.obelix.pi.repository.PontoColetaRepo;
import com.obelix.pi.repository.ResiduoRepo;
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
    ResiduoRepo residuoRepo;

    @Autowired
    private RotaRepo rotaRepo;

    @Autowired
    private BairroRepo bairroRepo;

    @Override
    public Rota gerarRota(RotaRequestDTO requestDTO) {
        if(caminhaoService.validarCompatibilidadeComResiduos(requestDTO.getCaminhaoId(), requestDTO.getDestinoId()) &&
            caminhaoService.validarCompatibilidadeComResiduos(requestDTO.getCaminhaoId(), requestDTO.getOrigemId())) {
                Rota rota = new Rota();
                rota.setCaminhao(caminhaoRepo.getReferenceById(requestDTO.getCaminhaoId()));

                PontoColeta pontoColetaOrigem = pontoColetaRepo.getReferenceById(requestDTO.getOrigemId());
                Bairro bairroPontoColetaOrigem = bairroRepo.getReferenceById(pontoColetaOrigem.getBairro().getId());
                PontoColeta pontoColetaDestino = pontoColetaRepo.getReferenceById(requestDTO.getDestinoId());
                Bairro bairroPontoColetaDestino = bairroRepo.getReferenceById(pontoColetaDestino.getBairro().getId());

                List<Rua> caminho = dijkstraService.encontrarCaminhoMaisCurto(bairroPontoColetaOrigem.getId(), bairroPontoColetaDestino.getId());
                List<Bairro> bairroCaminho = extrairBairrosDoCaminho(caminho, bairroPontoColetaOrigem.getId(), bairroPontoColetaDestino.getId(), bairroRepo);
                rota.setBairros(bairroCaminho);
                rota.setRuas(caminho);
                rota.setTiposResiduos(residuoRepo.findAllById(requestDTO.getTipoResiduoId()));
                return rota;
        } throw new RuntimeException("Erro ao gerar rota: Tipo de residuo não compativel.");
    }

    @Override
    public void atualizarRotas() {
        List<Rota> rotas = rotaRepo.findAll();
        for (Rota rota : rotas) {
            Long caminhaoId = rota.getCaminhao().getId();
            Long pontoColetaOrigemId = rota.getRuas().get(0).getOrigem().getId();
            Long pontoColetaDestinoId = rota.getRuas().get(rota.getRuas().size() - 1).getDestino().getId();
            List<Residuo> tiposResiduos = rota.getTiposResiduos();

            RotaRequestDTO requestDTO = new RotaRequestDTO();
            requestDTO.setCaminhaoId(caminhaoId);
            requestDTO.setOrigemId(pontoColetaOrigemId);
            requestDTO.setDestinoId(pontoColetaDestinoId);

            // Assuming setTipoResiduoId expects a list of IDs
            List<Long> tipoResiduoIds = new ArrayList<>();
            for (Residuo residuo : tiposResiduos) {
                tipoResiduoIds.add(residuo.getId());
            }
            requestDTO.setTipoResiduoId(tipoResiduoIds);

            Rota rotaOtimizada = gerarRota(requestDTO);
            rota.setBairros(rotaOtimizada.getBairros());
            rota.setRuas(rotaOtimizada.getRuas());
            rota.setDistanciaTotal(rotaOtimizada.getDistanciaTotal());
            rotaRepo.save(rota);
        }
    }

    public List<Bairro> extrairBairrosDoCaminho(List<Rua> caminho, Long origemId, Long destinoId, BairroRepo bairroRepo) {
        List<Bairro> bairros = new ArrayList<>();
        if (caminho == null || caminho.isEmpty()) {
            // Caminho vazio, só origem e destino
            bairros.add(bairroRepo.findById(origemId).orElse(null));
            bairros.add(bairroRepo.findById(destinoId).orElse(null));
            return bairros;
        }
        // Sempre começa pelo bairro de origem
        Bairro atual = bairroRepo.findById(origemId).orElse(null);
        if (atual != null) bairros.add(atual);

        for (Rua rua : caminho) {
            Bairro destinoRua = rua.getDestino();
            if (destinoRua != null) bairros.add(destinoRua);
        }
        return bairros;
    }
}
package com.obelix.pi.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    private ResiduoRepo residuoRepo;

    @Autowired
    private RotaRepo rotaRepo;

    @Autowired
    private BairroRepo bairroRepo;

    @Override
    public Rota gerarRota(RotaRequestDTO requestDTO) {
        // Valida compatibilidade dos resíduos do caminhão com os pontos de coleta
        if(caminhaoService.validarCompatibilidadeComResiduos(requestDTO.getCaminhaoId(), requestDTO.getDestinoId()) &&
           caminhaoService.validarCompatibilidadeComResiduos(requestDTO.getCaminhaoId(), requestDTO.getOrigemId())) {

            Rota rota = new Rota();
            rota.setCaminhao(caminhaoRepo.getReferenceById(requestDTO.getCaminhaoId()));

            // Pega os bairros de origem e destino
            PontoColeta pontoColetaOrigem = pontoColetaRepo.getReferenceById(requestDTO.getOrigemId());
            Bairro bairroOrigem = bairroRepo.getReferenceById(pontoColetaOrigem.getBairro().getId());

            PontoColeta pontoColetaDestino = pontoColetaRepo.getReferenceById(requestDTO.getDestinoId());
            Bairro bairroDestino = bairroRepo.getReferenceById(pontoColetaDestino.getBairro().getId());

            // Calcula o caminho mais curto usando Dijkstra
            List<Rua> caminho = dijkstraService.encontrarCaminhoMaisCurto(bairroOrigem.getId(), bairroDestino.getId());
            List<Bairro> bairrosCaminho = extrairBairrosDoCaminho(caminho, bairroOrigem.getId(), bairroDestino.getId());

            rota.setBairros(bairrosCaminho);
            rota.setRuas(caminho);
            rota.setTiposResiduos(residuoRepo.findAllById(requestDTO.getTipoResiduoId()));

            return rota;
        }

        throw new RuntimeException("Erro ao gerar rota: Tipo de resíduo não compatível com o caminhão.");
    }

    @Override
    public void atualizarRotas() {
        List<Rota> rotas = rotaRepo.findAll();

        for (Rota rota : rotas) {
            // Cria o DTO para gerar rota otimizada
            RotaRequestDTO requestDTO = new RotaRequestDTO(
                rota.getRuas().get(0).getOrigem().getId(),
                rota.getRuas().get(rota.getRuas().size() - 1).getDestino().getId(),
                rota.getTiposResiduos().stream().map(Residuo::getId).toList(),
                rota.getCaminhao().getId()
            );

            Rota rotaOtimizada = gerarRota(requestDTO);

            rota.setBairros(rotaOtimizada.getBairros());
            rota.setRuas(rotaOtimizada.getRuas());
            rota.setDistanciaTotal(rotaOtimizada.getDistanciaTotal());

            rotaRepo.save(rota);
        }
    }

    private List<Bairro> extrairBairrosDoCaminho(List<Rua> caminho, Long origemId, Long destinoId) {
        List<Bairro> bairros = new ArrayList<>();
        if (caminho == null || caminho.isEmpty()) {
            bairros.add(bairroRepo.findById(origemId).orElse(null));
            bairros.add(bairroRepo.findById(destinoId).orElse(null));
            return bairros;
        }

        Bairro atual = bairroRepo.findById(origemId).orElse(null);
        if (atual != null) bairros.add(atual);

        for (Rua rua : caminho) {
            Bairro destinoRua = rua.getDestino();
            if (destinoRua != null) bairros.add(destinoRua);
        }

        return bairros;
    }
}

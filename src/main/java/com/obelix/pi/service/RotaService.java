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
        if (requestDTO == null) throw new RuntimeException("Request inválido");

        // validações de compatibilidade
        boolean okOrigem = caminhaoService.validarCompatibilidadeComResiduos(requestDTO.getCaminhaoId(), requestDTO.getOrigemId());
        boolean okDestino = caminhaoService.validarCompatibilidadeComResiduos(requestDTO.getCaminhaoId(), requestDTO.getDestinoId());
        if (!(okOrigem && okDestino)) throw new RuntimeException("Tipo de resíduo não compatível com o caminhão.");

        Rota rota = new Rota();
        rota.setCaminhao(caminhaoRepo.findById(requestDTO.getCaminhaoId())
                .orElseThrow(() -> new RuntimeException("Caminhão não encontrado")));

        PontoColeta pontoOrigem = pontoColetaRepo.findById(requestDTO.getOrigemId())
                .orElseThrow(() -> new RuntimeException("Ponto de coleta origem não encontrado"));
        PontoColeta pontoDestino = pontoColetaRepo.findById(requestDTO.getDestinoId())
                .orElseThrow(() -> new RuntimeException("Ponto de coleta destino não encontrado"));

        Bairro bairroOrigem = pontoOrigem.getBairro();
        Bairro bairroDestino = pontoDestino.getBairro();

        List<Rua> caminho = dijkstraService.encontrarCaminhoMaisCurto(bairroOrigem.getId(), bairroDestino.getId());
        List<Bairro> bairrosCaminho = extrairBairrosDoCaminho(caminho, bairroOrigem.getId(), bairroDestino.getId());

        rota.setBairros(bairrosCaminho);
        rota.setRuas(caminho);

        // tipos de residuos
        List<Residuo> tipos = residuoRepo.findAllById(requestDTO.getTipoResiduoId());
        rota.setTiposResiduos(tipos);

        // calcula distancia total
        double distanciaTotal = caminho.stream().mapToDouble(Rua::getDistanciaKm).sum();
        rota.setDistanciaTotal(distanciaTotal);

        return rota;
    }

    @Override
    public void atualizarRotas() {
        List<Rota> rotas = rotaRepo.findAll();

        for (Rota rota : rotas) {
            try {
                // determinar origem e destino a partir das ruas atuais (fallback para bairros se ruas estiverem vazias)
                Long origemId = null;
                Long destinoId = null;

                if (rota.getRuas() != null && !rota.getRuas().isEmpty()) {
                    List<Rua> ruas = rota.getRuas();
                    origemId = ruas.get(0).getOrigem().getId();
                    destinoId = ruas.get(ruas.size() - 1).getDestino().getId();
                } else if (rota.getBairros() != null && !rota.getBairros().isEmpty()) {
                    List<Bairro> bairros = rota.getBairros();
                    origemId = bairros.get(0).getId();
                    destinoId = bairros.get(bairros.size() - 1).getId();
                } else {
                    // não há dados para recalcular -> pular
                    continue;
                }

                List<Long> tipoResiduoIds = rota.getTiposResiduos() == null ? new ArrayList<>()
                        : rota.getTiposResiduos().stream().map(Residuo::getId).collect(Collectors.toList());

                if (rota.getCaminhao() == null || rota.getCaminhao().getId() == null) {
                    // sem caminhão atribuído -> pular
                    continue;
                }

                RotaRequestDTO dto = new RotaRequestDTO();
                dto.setOrigemId(origemId);
                dto.setDestinoId(destinoId);
                dto.setTipoResiduoId(tipoResiduoIds);
                dto.setCaminhaoId(rota.getCaminhao().getId());

                Rota otimizada = gerarRota(dto);

                rota.setBairros(otimizada.getBairros());
                rota.setRuas(otimizada.getRuas());
                rota.setTiposResiduos(otimizada.getTiposResiduos());
                double distanciaTotal = otimizada.getRuas() == null ? 0.0 : otimizada.getRuas().stream().mapToDouble(Rua::getDistanciaKm).sum();
                rota.setDistanciaTotal(distanciaTotal);

                rotaRepo.save(rota);
            } catch (Exception e) {
                // log e continuar (não interrompe atualização em massa)
                System.err.println("[RotaService] Falha ao atualizar rota id=" + rota.getId() + " : " + e.getMessage());
            }
        }
    }

    private List<Bairro> extrairBairrosDoCaminho(List<Rua> caminho, Long origemId, Long destinoId) {
        List<Bairro> bairros = new ArrayList<>();
        if (caminho == null || caminho.isEmpty()) {
            bairroRepo.findById(origemId).ifPresent(bairros::add);
            bairroRepo.findById(destinoId).ifPresent(bairros::add);
            return bairros;
        }

        bairroRepo.findById(origemId).ifPresent(bairros::add);
        for (Rua rua : caminho) {
            if (rua.getDestino() != null) bairros.add(rua.getDestino());
        }
        return bairros;
    }
}

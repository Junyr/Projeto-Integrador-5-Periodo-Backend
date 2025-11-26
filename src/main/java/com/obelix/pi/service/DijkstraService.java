package com.obelix.pi.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.obelix.pi.model.Bairro;
import com.obelix.pi.model.Rua;
import com.obelix.pi.repository.BairroRepo;
import com.obelix.pi.repository.PontoColetaRepo;
import com.obelix.pi.repository.RuaRepo;

@Service
// Padrão Singleton: Garante apenas uma instância do serviço
public class DijkstraService {
    
    private static DijkstraService instance;

    private DijkstraService() {}

    @Autowired
    private RuaRepo ruaRepo;

    @Autowired
    private BairroRepo bairroRepo;

    @Autowired
    private PontoColetaRepo pontoColetaRepo;

    public static DijkstraService getInstance() {
        if(instance == null) {
            instance = new DijkstraService();
        }
        return instance;
    }

    // Padrão Template Method: Estrutura do algoritmo de Dijkstra
    public List<Rua> encontrarCaminhoMaisCurto(Long origemId, Long destinoId) {
        Bairro origem = bairroRepo.findById(origemId).orElse(null);
        Bairro destino = bairroRepo.findById(destinoId).orElse(null);
        if (origem != null || destino != null) {
            List<Rua> ruas = ruaRepo.findAll();
            Map<Long, Double> distancias = new HashMap<>();
            Map<Long, Long> predecessores = new HashMap<>();
            PriorityQueue<Bairro> fila = new PriorityQueue<>(Comparator.comparingDouble(b -> distancias.getOrDefault(b.getId(), Double.MAX_VALUE)));

            distancias.put(origemId, 0.0);
            fila.add(origem);

            while (!fila.isEmpty()) {
                Bairro atual = fila.poll();
                if (atual.getId().equals(destinoId)) break;

                for (Rua rua : ruas) {
                    if (rua.getOrigem().equals(atual)) {
                        Bairro vizinho = rua.getDestino();
                        double novaDistancia = distancias.get(atual.getId()) + rua.getDistanciaKm();
                        if (novaDistancia < distancias.getOrDefault(vizinho.getId(), Double.MAX_VALUE)) {
                            distancias.put(vizinho.getId(), novaDistancia);
                            predecessores.put(vizinho.getId(), atual.getId());
                            fila.add(vizinho);
                        }
                    }
                }
            }

            List<Rua> caminho = new ArrayList<>();
            Long atualId = destinoId;
            while (atualId != null && !atualId.equals(origemId)) {
                Long predecessorId = predecessores.get(atualId);
                if (predecessorId != null) {
                    List<Rua> ruasEntre = ruaRepo.findByOrigemAndDestino(predecessorId, atualId);
                    Rua menorRua = ruasEntre.stream().min(Comparator.comparingDouble(Rua::getDistanciaKm)).orElse(null);
                    if (menorRua != null) caminho.add(0, menorRua);
                }
                atualId = predecessorId;
            }

            if (atualId != null && atualId.equals(origemId)) return caminho;
        }

        throw new RuntimeException("Caminho não encontrado entre os bairros especificados.");
    }
}

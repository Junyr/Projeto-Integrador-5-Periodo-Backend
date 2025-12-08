package com.obelix.pi.service;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.obelix.pi.model.Bairro;
import com.obelix.pi.model.Rua;
import com.obelix.pi.repository.BairroRepo;
import com.obelix.pi.repository.RuaRepo;

@Service
public class DijkstraService {

    @Autowired
    private RuaRepo ruaRepo;

    @Autowired
    private BairroRepo bairroRepo;

    /**
     * Implementação do algoritmo para encontrar caminho entre bairros.
     * Retorna lista de ruas que compõem o caminho.
     */
    public List<Rua> encontrarCaminhoMaisCurto(Long origemId, Long destinoId) {
        if (origemId == null || destinoId == null) throw new IllegalArgumentException("IDs não podem ser nulos");

        Bairro origem = bairroRepo.findById(origemId).orElse(null);
        Bairro destino = bairroRepo.findById(destinoId).orElse(null);
        if (origem == null || destino == null) throw new RuntimeException("Bairro origem/destino não encontrado");

        List<Rua> ruas = ruaRepo.findAll();
        // construir grafo simples
        Map<Long, List<Rua>> adj = new HashMap<>();
        for (Rua r : ruas) {
            if (r.getOrigem() == null || r.getDestino() == null) continue;
            adj.computeIfAbsent(r.getOrigem().getId(), k -> new ArrayList<>()).add(r);
        }

        Map<Long, Double> dist = new HashMap<>();
        Map<Long, Rua> prevRua = new HashMap<>();
        Set<Long> visited = new HashSet<>();
        PriorityQueue<Long> pq = new PriorityQueue<>(Comparator.comparingDouble(dist::get));

        dist.put(origemId, 0.0);
        pq.add(origemId);

        while (!pq.isEmpty()) {
            Long currentId = pq.poll();
            if (!visited.add(currentId)) continue;
            if (currentId.equals(destinoId)) break;

            List<Rua> adjRuas = adj.getOrDefault(currentId, Collections.emptyList());
            double curDist = dist.getOrDefault(currentId, Double.MAX_VALUE);

            for (Rua r : adjRuas) {
                Long neighborId = r.getDestino().getId();
                double nd = curDist + r.getDistanciaKm();
                if (nd < dist.getOrDefault(neighborId, Double.MAX_VALUE)) {
                    dist.put(neighborId, nd);
                    prevRua.put(neighborId, r);
                    pq.remove(neighborId); // atualizar prioridade
                    pq.add(neighborId);
                }
            }
        }

        if (!dist.containsKey(destinoId)) throw new RuntimeException("Caminho não encontrado entre os bairros especificados.");

        // reconstruir caminho de ruas
        LinkedList<Rua> caminho = new LinkedList<>();
        Long cur = destinoId;
        while (!cur.equals(origemId)) {
            Rua r = prevRua.get(cur);
            if (r == null) break;
            caminho.addFirst(r);
            cur = r.getOrigem().getId();
        }
        return caminho;
    }
}

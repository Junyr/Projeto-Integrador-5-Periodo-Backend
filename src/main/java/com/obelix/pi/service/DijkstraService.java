package com.obelix.pi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.obelix.pi.repository.BairroRepo;

import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

import com.obelix.pi.repository.RuaRepo;
import com.obelix.pi.repository.PontoColetaRepo;
import com.obelix.pi.model.Bairro;
import com.obelix.pi.model.Rua;

@Service
public class DijkstraService {
    
    @Autowired
    RuaRepo ruaRepo;

    @Autowired
    BairroRepo bairroRepo;

    @Autowired
    PontoColetaRepo pontoColetaRepo;

    public List<Rua> encontrarCaminhoMaisCurto(Long origemId, Long destinoId) {
        Bairro origem = bairroRepo.findById(origemId).orElse(null);
        Bairro destino = bairroRepo.findById(destinoId).orElse(null);
        if (origem != null || destino != null) {
            List<Rua> ruas = ruaRepo.findAll();
            // Implementar o algoritmo de Dijkstra aqui
            // Exemplo de implementação simplificada:
            // 1. Criar um mapa de distâncias e um mapa de predecessores
            // 2. Inicializar as distâncias com infinito, exceto a origem que é 0
            // 3. Usar uma fila de prioridade para explorar os nós
            // 4. Atualizar as distâncias dos vizinhos e predecessores
            // 5. Repetir até que todos os nós sejam processados ou o destino seja alcançado
            // 6. Reconstruir o caminho a partir do destino até a origem usando o mapa de predecessores
            // 7. Retornar a lista de ruas representando o caminho mais curto
            // Exemplo de código simplificado:
            Map<Long, Double> distancias = new HashMap<>();
            Map<Long, Long> predecessores = new HashMap<>();
            PriorityQueue<Bairro> fila = new PriorityQueue<>(Comparator.comparingDouble(b -> distancias.getOrDefault(b.getId(), Double.MAX_VALUE)));
            distancias.put(origemId, 0.0);
            fila.add(origem);
            while (!fila.isEmpty()) {
                Bairro atual = fila.poll();
                if (atual.getId().equals(destinoId)) {
                    break;
                }
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
            // Reconstruir o caminho
            List<Rua> caminho = new ArrayList<>();
            Long atualId = destinoId;
            while (atualId != null && !atualId.equals(origemId)) {
                Long predecessorId = predecessores.get(atualId);
                if (predecessorId != null) {
                    List<Rua> ruasEntre = ruaRepo.findByOrigemAndDestino(predecessorId, atualId);
                    if (ruasEntre != null && !ruasEntre.isEmpty()) {
                        Rua menorRua = ruasEntre.stream()
                            .min(Comparator.comparingDouble(Rua::getDistanciaKm))
                            .orElse(null);
                        if (menorRua != null) {
                            caminho.add(0, menorRua);
                        } // ou escolha a lógica desejada
                    }// Adicionar no início da lista
                }
                atualId = predecessorId;
            }
            if (atualId != null && atualId.equals(origemId)) {
                return caminho; // Retornar o caminho encontrado
            }
        }
        // Se não for possível encontrar um caminho, retornar uma lista vazia
        throw new RuntimeException("Caminho não encontrado entre os bairros especificados.");
    }
}

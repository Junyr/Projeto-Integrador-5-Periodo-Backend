package com.obelix.pi.repository;

import com.opencsv.CSVReader;
import com.obelix.pi.model.Bairro;
import com.obelix.pi.model.PontoColeta;
import com.obelix.pi.model.Residuo;
import com.obelix.pi.model.Rua;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Component
public class DataInitializer {

    @PersistenceContext
    private EntityManager em;

    @PostConstruct
    @Transactional
    public void init() {
        try {
            importarBairros();
            importarRuas();
            importarPontosColeta();
            System.out.println("Importação concluída com sucesso.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void importarBairros() throws Exception {
        InputStream is = getClass().getResourceAsStream("/data/bairros.csv");
        if (is == null) {
            throw new FileNotFoundException("Arquivo bairros.csv não encontrado no classpath.");
        }
        try (CSVReader reader = new CSVReader(new InputStreamReader(is))) {
            reader.readNext(); // Pula cabeçalho
            String[] linha;

            while ((linha = reader.readNext()) != null) {
                // Ignora o id da coluna 0 e usa apenas o nome do bairro
                String nome = linha[1].trim(); // coluna "nome_bairro"

                Bairro bairro = new Bairro(null, nome);
                em.persist(bairro);
            }
        }
    }

    private void importarRuas() throws Exception {
        try (CSVReader reader = new CSVReader(new FileReader("/data/ruas_conexoes.csv"))) {
            reader.readNext(); // Pula o cabeçalho
            String[] linha;

            while ((linha = reader.readNext()) != null) {
                // Colunas do CSV: id,bairro_origem_id,bairro_destino_id,distancia_km
                // Vamos ignorar o id, pois é gerado automaticamente
                Long bairroOrigemId = Long.parseLong(linha[1].trim());
                Long bairroDestinoId = Long.parseLong(linha[2].trim());
                double distanciaKm = Double.parseDouble(linha[3].trim());

                Bairro origem = em.find(Bairro.class, bairroOrigemId);
                if (origem == null) {
                    throw new RuntimeException("Bairro origem com id " + bairroOrigemId + " não encontrado.");
                }

                Bairro destino = em.find(Bairro.class, bairroDestinoId);
                if (destino == null) {
                    throw new RuntimeException("Bairro destino com id " + bairroDestinoId + " não encontrado.");
                }

                Rua rua = new Rua(null, origem, destino, distanciaKm);
                em.persist(rua);
            }
        }
    }


    private void importarPontosColeta() throws Exception {
        try (CSVReader reader = new CSVReader(new FileReader("/data/pontos_coleta.csv"))) {
            reader.readNext(); // Pula cabeçalho
            String[] linha;

            while ((linha = reader.readNext()) != null) {
                Long bairroId = Long.parseLong(linha[1].trim());
                String nome = linha[2].trim();
                String responsavel = linha[3].trim();
                String telefone = linha[4].trim();
                String email = linha[5].trim();
                String endereco = linha[6].trim();
                String horario = linha[7].trim();
                String[] tiposResiduo = linha[8].split(",");

                Bairro bairro = em.find(Bairro.class, bairroId);
                if (bairro == null) {
                    throw new RuntimeException("Bairro com id " + bairroId + " não encontrado.");
                }

                List<Residuo> residuos = new ArrayList<>();
                for (String tipo : tiposResiduo) {
                    tipo = tipo.trim();
                    Residuo residuo = buscarOuCriarResiduo(tipo);
                    residuos.add(residuo);
                }

                PontoColeta ponto = new PontoColeta(null, nome, responsavel, telefone, email, endereco, horario, bairro, residuos);
                em.persist(ponto);
            }
        }
    }

    private Residuo buscarOuCriarResiduo(String tipo) {
        List<Residuo> resultados = em.createQuery("SELECT r FROM Residuo r WHERE r.tipo = :tipo", Residuo.class)
                                    .setParameter("tipo", tipo)
                                    .getResultList();
        if (!resultados.isEmpty()) {
            return resultados.get(0);
        }
        Residuo novo = new Residuo(null, tipo);
        em.persist(novo);
        return novo;
    }
}

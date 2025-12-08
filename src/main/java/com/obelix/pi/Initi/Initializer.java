package com.obelix.pi.Initi;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.obelix.pi.model.Bairro;
import com.obelix.pi.model.PontoColeta;
import com.obelix.pi.model.Residuo;
import com.obelix.pi.model.Rua;
import com.obelix.pi.repository.BairroRepo;
import com.obelix.pi.repository.PontoColetaRepo;
import com.obelix.pi.repository.ResiduoRepo;
import com.obelix.pi.repository.RuaRepo;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Initializer - popula dados iniciais a partir de CSVs.
 *
 * Padrões aplicados (marcados nos comentários):
 * - Template Method: run() define o fluxo.
 * - Factory Method: criarResiduo(...)
 * - Strategy + Singleton: CsvParserStrategy + CsvParserFactory
 * - Builder (simplificado): métodos auxiliares createBairro/createRua/createPontoColeta
 *
 * Observação: substituí inner private static classes dos builders por métodos auxiliares para
 * simplificar como você pediu.
 */
@Component
public class Initializer implements CommandLineRunner {

    @Autowired
    private BairroRepo bairroRepo;

    @Autowired
    private RuaRepo ruaRepo;

    @Autowired
    private PontoColetaRepo pontoColetaRepo;

    @Autowired
    private ResiduoRepo residuoRepo;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("[Initializer] Iniciando rotina de bootstrap...");
        inicializarResiduos();       // Template Method passo 1 (Factory Method dentro)
        importarBairrosCSV();        // passo 2
        importarRuasCSV();           // passo 3
        importarPontoColetaCSV();    // passo 4
        System.out.println("[Initializer] Rotina de bootstrap finalizada.");
    }

    // ------------------------------
    // Factory Method (Residuo)
    // ------------------------------
    private Residuo criarResiduo(String tipo) {
        Residuo residuo = new Residuo();
        residuo.setTipo(tipo);
        return residuo;
    }

    private void inicializarResiduos() {
        String[] tipos = {"Metal", "Plástico", "Papel", "Orgânico"};
        for (String tipo : tipos) {
            boolean existe = false;
            try {
                existe = residuoRepo.existsByTipo(tipo);
            } catch (Exception e) {
                Residuo r = null;
                try { r = residuoRepo.findByTipo(tipo); } catch (Exception ignored) {}
                existe = (r != null);
            }
            if (!existe) {
                residuoRepo.save(criarResiduo(tipo));
                System.out.println("[Initializer] Resíduo adicionado: " + tipo);
            }
        }
    }

    // ------------------------------
    // Strategy + Singleton para CSV parsing (mantido)
    // ------------------------------
    private interface CsvParserStrategy {
        Iterator<String[]> parse(InputStream in) throws IOException;
    }

    private static class OpenCsvStrategy implements CsvParserStrategy {
        @Override
        public Iterator<String[]> parse(InputStream in) throws IOException {
            try {
                Reader reader = new InputStreamReader(in, StandardCharsets.UTF_8);
                CSVReader csvReader = new CSVReader(reader);
                List<String[]> all = new ArrayList<>();
                String[] next;
                while ((next = csvReader.readNext()) != null) {
                    all.add(next);
                }
                csvReader.close();
                return all.iterator();
            } catch (CsvValidationException e) {
                throw new IOException("Erro ao validar CSV: " + e.getMessage(), e);
            }
        }
    }

    private static class SimpleCsvStrategy implements CsvParserStrategy {
        @Override
        public Iterator<String[]> parse(InputStream in) throws IOException {
            BufferedReader br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
            List<String[]> linhas = new ArrayList<>();
            String linha;
            while ((linha = br.readLine()) != null) {
                linhas.add(linha.split(","));
            }
            br.close();
            return linhas.iterator();
        }
    }

    private static class CsvParserFactory {
        private static final CsvParserFactory INSTANCE = new CsvParserFactory();
        private final CsvParserStrategy strategy;

        private CsvParserFactory() {
            CsvParserStrategy s;
            try {
                Class.forName("com.opencsv.CSVReader");
                s = new OpenCsvStrategy();
            } catch (ClassNotFoundException e) {
                s = new SimpleCsvStrategy();
            }
            this.strategy = s;
        }

        public static CsvParserFactory getInstance() {
            return INSTANCE;
        }

        public CsvParserStrategy getStrategy() {
            return strategy;
        }
    }

    // ------------------------------
    // Métodos auxiliares "Builder-like"
    // ------------------------------
    private Bairro createBairro(Long id, String nome) {
        Bairro b = new Bairro();
        if (id != null) b.setId(id);
        b.setNome(nome);
        return b;
    }

    private Rua createRua(Long id, Bairro origem, Bairro destino, double distanciaKm) {
        Rua r = new Rua();
        if (id != null) r.setId(id);
        r.setOrigem(origem);
        r.setDestino(destino);
        r.setDistanciaKm(distanciaKm);
        return r;
    }

    private PontoColeta createPontoColeta(Long id, String nome, String responsavel, String telefone,
                                          String email, String endereco, String horario,
                                          Bairro bairro, List<Residuo> tipos) {
        PontoColeta p = new PontoColeta();
        if (id != null) p.setId(id);
        p.setNome(nome);
        p.setResponsavel(responsavel);
        p.setTelefoneResponsavel(telefone);
        p.setEmailResponsavel(email);
        p.setEndereco(endereco);
        p.setHorario(horario);
        p.setBairro(bairro);
        p.setTiposResiduos(tipos);
        return p;
    }

    // ------------------------------
    // Import CSVs
    // ------------------------------
    private void importarBairrosCSV() {
        String path = "src/main/resources/data/bairros.csv";
        File f = new File(path);
        if (!f.exists()) {
            System.out.println("[Initializer] Arquivo não encontrado: " + path + " — pulando importação de bairros.");
            return;
        }

        try (InputStream in = new FileInputStream(f)) {
            Iterator<String[]> it = CsvParserFactory.getInstance().getStrategy().parse(in);
            boolean maybeHeaderChecked = false;
            while (it.hasNext()) {
                String[] cols = it.next();
                if (cols == null || cols.length == 0) continue;

                if (!maybeHeaderChecked) {
                    maybeHeaderChecked = true;
                    boolean isHeader = Arrays.stream(cols)
                            .anyMatch(s -> s != null && (s.toLowerCase().contains("nome") || s.toLowerCase().contains("id")));
                    if (isHeader) continue;
                }

                if (cols.length < 2) {
                    System.out.println("[Initializer] Linha inválida em bairros.csv: " + Arrays.toString(cols));
                    continue;
                }

                String idRaw = cols[0].trim();
                String nome = cols[1].trim();
                if (nome.isEmpty()) continue;

                Long bairroId;
                try { bairroId = Long.parseLong(idRaw); } catch (NumberFormatException ex) {
                    System.out.println("[Initializer] ID inválido em bairros.csv: " + idRaw);
                    continue;
                }

                if (bairroRepo.existsById(bairroId)) continue;
                Bairro bairro = createBairro(bairroId, nome);
                bairroRepo.save(bairro);
                System.out.println("[Initializer] Bairro adicionado: id=" + bairroId + ", nome=\"" + nome + "\"");
            }
        } catch (Exception e) {
            System.err.println("[Initializer] Erro ao importar bairros: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void importarRuasCSV() {
        String path = "src/main/resources/data/ruas_conexoes.csv";
        File f = new File(path);
        if (!f.exists()) {
            System.out.println("[Initializer] Arquivo não encontrado: " + path + " — pulando importação de ruas.");
            return;
        }

        try (InputStream in = new FileInputStream(f)) {
            Iterator<String[]> it = CsvParserFactory.getInstance().getStrategy().parse(in);
            boolean maybeHeaderChecked = false;
            while (it.hasNext()) {
                String[] cols = it.next();
                if (cols == null || cols.length == 0) continue;

                if (!maybeHeaderChecked) {
                    maybeHeaderChecked = true;
                    boolean isHeader = Arrays.stream(cols)
                            .anyMatch(s -> s != null && (s.toLowerCase().contains("origem") || s.toLowerCase().contains("destino") || s.toLowerCase().contains("id")));
                    if (isHeader) continue;
                }

                if (cols.length < 4) {
                    System.out.println("[Initializer] Linha inválida em ruas_conexoes.csv: " + Arrays.toString(cols));
                    continue;
                }

                String idRaw = cols[0].trim();
                String origemRaw = cols[1].trim();
                String destinoRaw = cols[2].trim();
                String distanciaRaw = cols[3].trim();

                Long ruaId, origemId, destinoId;
                double distanciaKm;
                try {
                    ruaId = Long.parseLong(idRaw);
                    origemId = Long.parseLong(origemRaw);
                    destinoId = Long.parseLong(destinoRaw);
                    distanciaKm = Double.parseDouble(distanciaRaw);
                } catch (Exception ex) {
                    System.out.println("[Initializer] Dados inválidos em ruas_conexoes.csv: " + Arrays.toString(cols));
                    continue;
                }

                if (ruaRepo.existsById(ruaId)) continue;

                Bairro origem = bairroRepo.findById(origemId).orElse(null);
                Bairro destino = bairroRepo.findById(destinoId).orElse(null);
                if (origem == null || destino == null) {
                    System.out.println("[Initializer] Bairro origem/destino não encontrado para rua id=" + ruaId);
                    continue;
                }

                Rua rua = createRua(ruaId, origem, destino, distanciaKm);
                ruaRepo.save(rua);
                System.out.println("[Initializer] Rua adicionada: id=" + ruaId + " entre \"" + origem.getNome() + "\" e \"" + destino.getNome() + "\" (" + distanciaKm + " km)");
            }
        } catch (Exception e) {
            System.err.println("[Initializer] Erro ao importar ruas: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void importarPontoColetaCSV() {
        String path = "src/main/resources/data/pontos_coleta.csv";
        File f = new File(path);
        if (!f.exists()) {
            System.out.println("[Initializer] Arquivo não encontrado: " + path + " — pulando importação de pontos de coleta.");
            return;
        }

        try (InputStream in = new FileInputStream(f)) {
            Iterator<String[]> it = CsvParserFactory.getInstance().getStrategy().parse(in);
            boolean maybeHeaderChecked = false;
            while (it.hasNext()) {
                String[] cols = it.next();
                if (cols == null || cols.length == 0) continue;

                if (!maybeHeaderChecked) {
                    maybeHeaderChecked = true;
                    boolean isHeader = Arrays.stream(cols)
                            .anyMatch(s -> s != null && (s.toLowerCase().contains("nome") || s.toLowerCase().contains("bairro") || s.toLowerCase().contains("residuo")));
                    if (isHeader) continue;
                }

                if (cols.length < 9) {
                    System.out.println("[Initializer] Linha inválida em pontos_coleta.csv: " + Arrays.toString(cols));
                    continue;
                }

                String idRaw = cols[0].trim();
                String bairroIdRaw = cols[1].trim();
                String nome = cols[2].trim();
                String responsavel = cols[3].trim();
                String telefone = cols[4].trim();
                String email = cols[5].trim();
                String endereco = cols[6].trim();
                String horario = cols[7].trim();
                String tiposResiduosRaw = cols[8].trim();

                Long pontoId, bairroId;
                try {
                    pontoId = Long.parseLong(idRaw);
                    bairroId = Long.parseLong(bairroIdRaw);
                } catch (NumberFormatException ex) {
                    System.out.println("[Initializer] ID inválido em pontos_coleta.csv: " + Arrays.toString(cols));
                    continue;
                }

                boolean existePorNome = false;
                try { existePorNome = pontoColetaRepo.existsByNome(nome); } catch (Exception ignored) {}
                if (existePorNome) continue;

                Bairro bairro = bairroRepo.findById(bairroId).orElse(null);
                if (bairro == null) {
                    System.out.println("[Initializer] Bairro não encontrado para ponto \"" + nome + "\" (bairroId=" + bairroId + ")");
                    continue;
                }

                List<Residuo> tiposResiduos = Arrays.stream(tiposResiduosRaw.split(","))
                        .map(String::trim)
                        .filter(s -> !s.isEmpty())
                        .map(tipoNome -> {
                            Residuo r = null;
                            try { r = residuoRepo.findByTipo(tipoNome); } catch (Exception ignored) {}
                            if (r == null) {
                                try { r = residuoRepo.findByTipo(tipoNome.toLowerCase()); } catch (Exception ignored) {}
                            }
                            if (r == null) System.out.println("[Initializer] Resíduo não encontrado ao importar ponto \"" + nome + "\": " + tipoNome);
                            return r;
                        })
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList());

                PontoColeta ponto = createPontoColeta(pontoId, nome, responsavel, telefone, email, endereco, horario, bairro, tiposResiduos);
                pontoColetaRepo.save(ponto);
                System.out.println("[Initializer] Ponto de coleta adicionado: id=" + pontoId + ", nome=\"" + nome + "\"");
            }
        } catch (Exception e) {
            System.err.println("[Initializer] Erro ao importar pontos de coleta: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

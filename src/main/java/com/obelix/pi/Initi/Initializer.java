package com.obelix.pi.Initi;

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
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Initializer - popula dados iniciais a partir de CSVs.
 *
 * Padrões de projeto aplicados (comentados abaixo onde aparecem):
 * 1) Template Method  - método run() define o fluxo fixo de inicialização.
 * 2) Factory Method   - criarResiduo(...) encapsula criação de objetos Residuo.
 * 3) Strategy         - CsvParserStrategy interface permite trocar a implementação de parsing CSV.
 * 4) Singleton        - CsvParserFactory (instância única da estratégia).
 * 5) Builder          - Builders estáticos para criar objetos complexos (BairroBuilder, RuaBuilder, PontoColetaBuilder).
 *
 * Observações/Assunções:
 * - As entidades (Bairro, Rua, PontoColeta, Residuo) possuem setters (inclusive setId) caso queira preservar IDs do CSV.
 * - Repositórios (bairroRepo, ruaRepo, etc.) devem expor os métodos usados (existsById, existsByNome, findByTipo, etc.).
 *   (Se o nome dos métodos for diferente no seu projeto, eu ajusto quando me informar.)
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

    /**
     * Template Method: define a sequência de passos do fluxo de inicialização.
     * Alterações: cada passo é delegada a um método claro e separado.
     */
    @Override
    public void run(String... args) throws Exception {
        System.out.println("[Initializer] Iniciando rotina de bootstrap...");
        inicializarResiduos();       // passo 1 - Factory Method usado aqui
        importarBairrosCSV();        // passo 2
        importarRuasCSV();           // passo 3
        importarPontoColetaCSV();    // passo 4
        System.out.println("[Initializer] Rotina de bootstrap finalizada.");
    }

    // ------------------------------
    // Factory Method (Criação de Resíduo)
    // ------------------------------
    /**
     * Factory Method simples que encapsula a criação de um Residuo.
     * Se futuramente precisar inicializar com outras propriedades, altera-se somente aqui.
     */
    private Residuo criarResiduo(String tipo) {
        Residuo residuo = new Residuo();
        residuo.setTipo(tipo);
        return residuo;
    }

    /**
     * Inicializa resíduos padrão se ainda não existirem no banco.
     * Observação: usa residuoRepo.existsByTipo(...) - caso seu repo tenha assinatura diferente,
     * adapte para existsByTipoIgnoreCase ou similar.
     */
    private void inicializarResiduos() {
        String[] tipos = {"Metal", "Plástico", "Papel", "Orgânico"};
        for (String tipo : tipos) {
            // recomendação: ter um método case-insensitive no repo (existsByTipoIgnoreCase)
            boolean existe = false;
            try {
                existe = residuoRepo.existsByTipo(tipo);
            } catch (Exception e) {
                // fallback: tentar buscar por tipo ignorando case (caso o repo não tenha existsByTipo)
                try {
                    Residuo r = residuoRepo.findByTipo(tipo);
                    existe = (r != null);
                } catch (Exception ignored) {}
            }

            if (!existe) {
                residuoRepo.save(criarResiduo(tipo));
                System.out.println("[Initializer] Resíduo adicionado: " + tipo);
            }
        }
    }

    // ------------------------------
    // Strategy + Singleton (CSV Parsing)
    // ------------------------------
    /**
     * CsvParserStrategy - Strategy pattern: permite trocar a implementação do parser CSV.
     * A implementação padrão usa OpenCSV (OpenCsvStrategy). Em ambiente sem OpenCSV, poderíamos usar SimpleCsvStrategy.
     */
    private interface CsvParserStrategy {
        /**
         * Retorna um Iterator<String[]> lendo do InputStream (cada String[] é uma linha com colunas).
         * Implementações devem fechar recursos internamente ou devolver estruturas seguras.
         */
        Iterator<String[]> parse(InputStream in) throws IOException;
    }

    /**
     * OpenCsvStrategy - usa a biblioteca OpenCSV para parsing.
     */
    private static class OpenCsvStrategy implements CsvParserStrategy {
        @Override
        public Iterator<String[]> parse(InputStream in) throws IOException {
            try {
                Reader reader = new InputStreamReader(in, StandardCharsets.UTF_8);
                CSVReader csvReader = new CSVReader(reader);
                // Consumir todas as linhas para criar um Iterator imutável (facilita fechamento do stream)
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

    /**
     * SimpleCsvStrategy - fallback simples caso OpenCSV falhe.
     * (Se OpenCSV estiver disponível, não será usado.)
     */
    private static class SimpleCsvStrategy implements CsvParserStrategy {
        @Override
        public Iterator<String[]> parse(InputStream in) throws IOException {
            BufferedReader br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
            List<String[]> linhas = new ArrayList<>();
            String linha;
            while ((linha = br.readLine()) != null) {
                // split simples por vírgula (não suporta campos com vírgula entre aspas)
                linhas.add(linha.split(","));
            }
            br.close();
            return linhas.iterator();
        }
    }

    /**
     * CsvParserFactory - Singleton que fornece a estratégia de parsing.
     * Implementa Singleton (instância única), retornando a melhor estratégia disponível.
     */
    private static class CsvParserFactory {
        private static final CsvParserFactory INSTANCE = new CsvParserFactory();
        private final CsvParserStrategy strategy;

        private CsvParserFactory() {
            // tenta usar OpenCSV, senão usa fallback simples
            CsvParserStrategy s;
            try {
                // se a classe CSVReader existir no classpath, usamos OpenCsvStrategy
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
    // Builders (Builder Pattern)
    // ------------------------------
    /**
     * Builder para Bairro (melhora legibilidade na construção e possibilidade de validação antes do save).
     */
    private static class BairroBuilder {
        private final Bairro bairro = new Bairro();

        public BairroBuilder id(Long id) { bairro.setId(id); return this; }
        public BairroBuilder nome(String nome) { bairro.setNome(nome); return this; }
        public Bairro build() { return bairro; }
    }

    /**
     * Builder para Rua
     */
    private static class RuaBuilder {
        private final Rua rua = new Rua();

        public RuaBuilder id(Long id) { rua.setId(id); return this; }
        public RuaBuilder origem(Bairro origem) { rua.setOrigem(origem); return this; }
        public RuaBuilder destino(Bairro destino) { rua.setDestino(destino); return this; }
        public RuaBuilder distanciaKm(double km) { rua.setDistanciaKm(km); return this; }
        public Rua build() { return rua; }
    }

    /**
     * Builder para PontoColeta
     */
    private static class PontoColetaBuilder {
        private final PontoColeta p = new PontoColeta();

        public PontoColetaBuilder id(Long id) { p.setId(id); return this; }
        public PontoColetaBuilder nome(String nome) { p.setNome(nome); return this; }
        public PontoColetaBuilder responsavel(String r) { p.setResponsavel(r); return this; }
        public PontoColetaBuilder telefoneResponsavel(String t) { p.setTelefoneResponsavel(t); return this; }
        public PontoColetaBuilder emailResponsavel(String e) { p.setEmailResponsavel(e); return this; }
        public PontoColetaBuilder endereco(String end) { p.setEndereco(end); return this; }
        public PontoColetaBuilder horario(String h) { p.setHorario(h); return this; }
        public PontoColetaBuilder bairro(Bairro b) { p.setBairro(b); return this; }
        public PontoColetaBuilder tiposResiduos(List<Residuo> tipos) { p.setTiposResiduos(tipos); return this; }
        public PontoColeta build() { return p; }
    }

    // ------------------------------
    // Importação dos CSVs (uso da Strategy via CsvParserFactory)
    // ------------------------------

    /**
     * Importa bairros a partir do CSV: src/main/resources/data/bairros.csv
     * CSV esperado: id,nome (header opcional)
     */
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
                // pulando linhas completamente vazias
                if (cols == null || cols.length == 0) continue;

                // detectar header (somente na primeira linha)
                if (!maybeHeaderChecked) {
                    maybeHeaderChecked = true;
                    boolean isHeader = Arrays.stream(cols)
                            .anyMatch(s -> s != null && s.toLowerCase().contains("nome") || s.toLowerCase().contains("id"));
                    if (isHeader) continue;
                }

                // valida colunas
                if (cols.length < 2) {
                    System.out.println("[Initializer] Linha inválida em bairros.csv: " + Arrays.toString(cols));
                    continue;
                }

                String idRaw = cols[0].trim();
                String nome = cols[1].trim();

                if (nome.isEmpty()) {
                    System.out.println("[Initializer] Nome vazio ignorado (bairros.csv): " + Arrays.toString(cols));
                    continue;
                }

                Long bairroId;
                try {
                    bairroId = Long.parseLong(idRaw);
                } catch (NumberFormatException ex) {
                    System.out.println("[Initializer] ID inválido em bairros.csv: " + idRaw + " — ignorando linha.");
                    continue;
                }

                if (bairroRepo.existsById(bairroId)) {
                    // já existe com esse id -> ignora
                    continue;
                }

                Bairro bairro = new BairroBuilder()
                        .id(bairroId)
                        .nome(nome)
                        .build();

                bairroRepo.save(bairro);
                System.out.println("[Initializer] Bairro adicionado: id=" + bairroId + ", nome=\"" + nome + "\"");
            }
        } catch (Exception e) {
            System.err.println("[Initializer] Erro ao importar bairros: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Importa ruas a partir do CSV: src/main/resources/data/ruas_conexoes.csv
     * CSV esperado: id,origemId,destinoId,distanciaKm (header opcional)
     */
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

                Long ruaId;
                Long origemId;
                Long destinoId;
                double distanciaKm;
                try {
                    ruaId = Long.parseLong(idRaw);
                    origemId = Long.parseLong(origemRaw);
                    destinoId = Long.parseLong(destinoRaw);
                    distanciaKm = Double.parseDouble(distanciaRaw);
                } catch (Exception ex) {
                    System.out.println("[Initializer] Dados inválidos em ruas_conexoes.csv na linha: " + Arrays.toString(cols));
                    continue;
                }

                if (ruaRepo.existsById(ruaId)) {
                    continue; // já existe
                }

                Bairro origem = bairroRepo.findById(origemId).orElse(null);
                Bairro destino = bairroRepo.findById(destinoId).orElse(null);

                if (origem == null || destino == null) {
                    System.out.println("[Initializer] Bairro origem/destino não encontrado para rua id=" + ruaId + " (origem=" + origemId + ", destino=" + destinoId + ")");
                    continue;
                }

                Rua rua = new RuaBuilder()
                        .id(ruaId)
                        .origem(origem)
                        .destino(destino)
                        .distanciaKm(distanciaKm)
                        .build();

                ruaRepo.save(rua);
                System.out.println("[Initializer] Rua adicionada: id=" + ruaId + " entre \"" + origem.getNome() + "\" e \"" + destino.getNome() + "\" (" + distanciaKm + " km)");
            }
        } catch (Exception e) {
            System.err.println("[Initializer] Erro ao importar ruas: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Importa pontos de coleta a partir do CSV: src/main/resources/data/pontos_coleta.csv
     * CSV esperado (exemplo): id,bairroId,nome,responsavel,telefone,email,endereco,horario,tiposResiduos (onde tiposResiduos é "Metal,Plástico")
     *
     * Observação: o parsing dos tipos de resíduo tenta mapear para resíduos existentes via residuoRepo.findByTipo(...)
     * - Se algum tipo não existir, ele é ignorado para aquele ponto (com mensagem).
     */
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

                // espera ao menos 9 colunas: id, bairroId, nome, responsavel, telefone, email, endereco, horario, tiposResiduos
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

                Long pontoId;
                Long bairroId;
                try {
                    pontoId = Long.parseLong(idRaw);
                    bairroId = Long.parseLong(bairroIdRaw);
                } catch (NumberFormatException ex) {
                    System.out.println("[Initializer] ID inválido em pontos_coleta.csv: " + Arrays.toString(cols));
                    continue;
                }

                // detectar se ponto já existe pelo nome (recomenda-se existir método existsByNomeIgnoreCase)
                boolean existePorNome = false;
                try {
                    existePorNome = pontoColetaRepo.existsByNome(nome);
                } catch (Exception ex) {
                    // se não existir existsByNome, não trate como erro — apenas não faça a verificação
                }
                if (existePorNome) {
                    continue;
                }

                Bairro bairro = bairroRepo.findById(bairroId).orElse(null);
                if (bairro == null) {
                    System.out.println("[Initializer] Bairro não encontrado para ponto \"" + nome + "\" (bairroId=" + bairroId + ")");
                    continue;
                }

                // parse tipos de resíduo separados por vírgula e mapear para entidades Residuo existentes
                List<Residuo> tiposResiduos = Arrays.stream(tiposResiduosRaw.split(","))
                        .map(String::trim)
                        .filter(s -> !s.isEmpty())
                        .map(tipoNome -> {
                            // preferir método case-insensitive no repo (findByTipoIgnoreCase), caso não exista, tente findByTipo
                            Residuo r = null;
                            try {
                                r = residuoRepo.findByTipo(tipoNome);
                            } catch (Exception ignored) { }
                            if (r == null) {
                                try {
                                    r = residuoRepo.findByTipo(tipoNome.toLowerCase());
                                } catch (Exception ignored) { }
                            }
                            if (r == null) {
                                System.out.println("[Initializer] Resíduo não encontrado ao importar ponto \"" + nome + "\": " + tipoNome);
                            }
                            return r;
                        })
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList());

                PontoColeta ponto = new PontoColetaBuilder()
                        .id(pontoId)
                        .nome(nome)
                        .responsavel(responsavel)
                        .telefoneResponsavel(telefone)
                        .emailResponsavel(email)
                        .endereco(endereco)
                        .horario(horario)
                        .bairro(bairro)
                        .tiposResiduos(tiposResiduos)
                        .build();

                pontoColetaRepo.save(ponto);
                System.out.println("[Initializer] Ponto de coleta adicionado: id=" + pontoId + ", nome=\"" + nome + "\"");
            }
        } catch (Exception e) {
            System.err.println("[Initializer] Erro ao importar pontos de coleta: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // ------------------------------
    // Fim do Initializer
    // ------------------------------

    /*
     * Observações finais (boas práticas e notas):
     *
     * - Repository Pattern: o uso de bairroRepo, ruaRepo, etc. é a aplicação do Repository Pattern
     *   (abstrai a persistência). Mantenha os repositórios com métodos claros (existsBy..., findBy...).
     *
     * - IDs do CSV: aqui eu defini setId(...) quando o CSV fornece ID. Se preferir que o DB gere ids
     *   automaticamente (auto-increment), remova os .setId(...) dos Builders.
     *
     * - Validações adicionais: este Initializer faz validações básicas. Para validações mais rígidas,
     *   mova a lógica para Services específicos e escreva testes unitários.
     *
     * - Logs: por simplicidade, usei System.out.println. Em produção, substitua por um logger (SLF4J + Logback).
     *
     * - Métodos de repositório (ex.: existsByNome, findByTipo) podem não existir no seu código atual.
     *   Se esses métodos não existirem, crie-os nas interfaces dos repos (Spring Data JPA cria impl. automaticamente).
     *
     * Padrões aplicados e onde estão no código:
     * - Template Method: run() define o pipeline de inicialização (inicializarResiduos -> importar...).
     * - Factory Method: criarResiduo(...) encapsula criação de objetos Residuo.
     * - Strategy: CsvParserStrategy + OpenCsvStrategy/SimpleCsvStrategy permitem trocar o método de parsing CSV.
     * - Singleton: CsvParserFactory é uma instância única que fornece a estratégia de parsing.
     * - Builder: BairroBuilder, RuaBuilder, PontoColetaBuilder facilitam criação de entidades com validação e código legível.
     */
}

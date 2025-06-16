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
import java.io.*;
import java.util.List;
import java.util.stream.*;
import org.springframework.beans.factory.annotation.Autowired;

@Component
public class Initializer implements CommandLineRunner {

    // Exemplo de injeção de um repositório JPA
    @Autowired
    private BairroRepo bairroRepo;

    @Autowired
    private PontoColetaRepo pontoColetaRepo;

    @Autowired
    private RuaRepo ruaRepo;

    @Autowired
    private ResiduoRepo residuoRepo;

    @Override
    public void run(String... args) throws Exception {
        residuosIniciais();
        importarBairrosCSV();
        importarRuasCSV();
        importarPontoColetaCSV();
    }

    private void residuosIniciais() {
        if(!residuoRepo.existsByTipo("Metal")){
            Residuo metal = new Residuo();
            metal.setTipo("Metal");
            residuoRepo.save(metal);
            System.out.println("Reiduo adicionado: " + metal.getTipo());
        }

        if(!residuoRepo.existsByTipo("Plástico")){
            Residuo plastico = new Residuo();
            plastico.setTipo("Plástico");
            residuoRepo.save(plastico);
            System.out.println("Reiduo adicionado: " + plastico.getTipo());
        }

        if(!residuoRepo.existsByTipo("Papel")){
            Residuo papel = new Residuo();
            papel.setTipo("Papel");
            residuoRepo.save(papel);
            System.out.println("Reiduo adicionado: " + papel.getTipo());
        }

        if(!residuoRepo.existsByTipo("Orgânico")) {
            Residuo organico = new Residuo();
            organico.setTipo("Orgânico");
            residuoRepo.save(organico);
            System.out.println("Reiduo adicionado: " + organico.getTipo());
        }

    }

    private void importarBairrosCSV() throws CsvValidationException {
        // Lógica para ler o CSV, validar e salvar no banco
        // Exemplo:
        // 1. Ler linhas do CSV
        // 2. Para cada linha, verificar se já existe no banco
        // 3. Se não existir, salvar

        // Exemplo de leitura:
        try (CSVReader reader = new CSVReader(new InputStreamReader(new FileInputStream("src/main/resources/data/bairros.csv"), "UTF-8"))) {
            String[] line;
            boolean isHeader = true;
            while ((line = reader.readNext()) != null) {
                if (isHeader) {
                    isHeader = false; // Ignora o cabeçalho
                    continue;
                }
                Long bairroId = Long.parseLong(line[0]);
                String bairroNome = line[1];

                if(!bairroRepo.existsById(bairroId)) {
                    // Cria e salva o novo bairro
                    Bairro bairro = new Bairro();
                    bairro.setNome(bairroNome);
                    bairroRepo.save(bairro);
                    System.out.println("Bairro adicionado: " + bairroNome);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void importarRuasCSV() throws CsvValidationException {
        try (CSVReader reader = new CSVReader(new InputStreamReader(new FileInputStream("src/main/resources/data/ruas_conexoes.csv"), "UTF-8"))) {
            String[] line;
            boolean isHeader = true;
            while ((line = reader.readNext()) != null) {
                if (isHeader) {
                    isHeader = false;
                    continue;
                }
                Long ruaId = Long.parseLong(line[0]);

                if(!ruaRepo.existsById(ruaId)) {
                    Bairro bairroOrigem = bairroRepo.findById(Long.parseLong(line[1])).orElse(null);
                    Bairro bairroDestino = bairroRepo.findById(Long.parseLong(line[2])).orElse(null);
                    if (bairroOrigem == null || bairroDestino == null) {
                        System.out.println("Bairro não encontrado para a rua: " + ruaId);
                    } else {
                        Rua rua = new Rua();
                        rua.setOrigem(bairroOrigem);
                        rua.setDestino(bairroDestino);
                        Double distancia = Double.parseDouble(line[3]);
                        rua.setDistanciaKm(distancia);
                        ruaRepo.save(rua);
                        System.out.println("Rua adicionada: " + line[3]);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void importarPontoColetaCSV() throws CsvValidationException {
        try (CSVReader reader = new CSVReader(new InputStreamReader(new FileInputStream("src/main/resources/data/pontos_coleta.csv"), "UTF-8"))) {
            String[] line;
            boolean isHeader = true;
            while ((line = reader.readNext()) != null) {
                if (isHeader) {
                    isHeader = false;
                    continue;
                }
                String nome = line[2];
                if(!pontoColetaRepo.existsByNome(nome)) {
                    String responsavel = line[3];
                    String telefoneResponsavel = line[4];
                    String emailResponsavel = line[5];
                    String endereco = line[6];
                    String horario = line[7];

                    Bairro bairro = bairroRepo.findById(Long.parseLong(line[1])).orElse(null);

                    if (bairro == null) {
                        System.out.println("Bairro não encontrado para o ponto de coleta: " + nome);
                        continue; // Pula para a próxima linha se o bairro não for encontrado
                    } else {
                        String[] listaResiduo = line[8].split(",");
                        List<Residuo> tiposResiduos = Stream.of(listaResiduo)
                            .map(String::trim)
                            .map(residuoNome -> residuoRepo.findByTipo(residuoNome))
                            .filter(r -> r != null)
                            .collect(Collectors.toList());

                        // Cria e salva o novo bairro
                        PontoColeta pontoColeta = new PontoColeta();
                        pontoColeta.setNome(nome);
                        pontoColeta.setResponsavel(responsavel);
                        pontoColeta.setTelefoneResponsavel(telefoneResponsavel);
                        pontoColeta.setEmailResponsavel(emailResponsavel);
                        pontoColeta.setEndereco(endereco);
                        pontoColeta.setHorario(horario);
                        pontoColeta.setBairro(bairro);
                        pontoColeta.setTiposResiduos(tiposResiduos);
                        pontoColetaRepo.save(pontoColeta);
                        System.out.println("Ponto de coleta adicionado: " + nome);
                        }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

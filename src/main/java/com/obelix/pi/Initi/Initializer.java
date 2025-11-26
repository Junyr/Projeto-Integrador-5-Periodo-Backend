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
// Padrão Template Method: define o fluxo de inicialização
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
        inicializarResiduos();       // passo 1 do template
        importarBairrosCSV();        // passo 2 do template
        importarRuasCSV();           // passo 3 do template
        importarPontoColetaCSV();    // passo 4 do template
    }

    // Padrão Factory Method: criação de resíduos
    private Residuo criarResiduo(String tipo) {
        Residuo residuo = new Residuo();
        residuo.setTipo(tipo);
        return residuo;
    }

    // Template Method: inicialização de resíduos
    private void inicializarResiduos() {
        String[] tipos = {"Metal","Plástico","Papel","Orgânico"};
        for(String tipo : tipos) {
            if(!residuoRepo.existsByTipo(tipo)) {
                residuoRepo.save(criarResiduo(tipo));
                System.out.println("Resíduo adicionado: " + tipo);
            }
        }
    }

    private void importarBairrosCSV() throws CsvValidationException {
        try (CSVReader reader = new CSVReader(new InputStreamReader(new FileInputStream("src/main/resources/data/bairros.csv"), "UTF-8"))) {
            String[] line;
            boolean isHeader = true;
            while ((line = reader.readNext()) != null) {
                if (isHeader) { isHeader = false; continue; }
                Long bairroId = Long.parseLong(line[0]);
                String bairroNome = line[1];
                if(!bairroRepo.existsById(bairroId)) {
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
                if (isHeader) { isHeader = false; continue; }
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
                        rua.setDistanciaKm(Double.parseDouble(line[3]));
                        ruaRepo.save(rua);
                        System.out.println("Rua adicionada entre " + bairroOrigem.getNome() + " e " + bairroDestino.getNome());
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
                if (isHeader) { isHeader = false; continue; }

                String nome = line[2];
                if(!pontoColetaRepo.existsByNome(nome)) {
                    Bairro bairro = bairroRepo.findById(Long.parseLong(line[1])).orElse(null);
                    if (bairro == null) {
                        System.out.println("Bairro não encontrado para o ponto de coleta: " + nome);
                        continue;
                    }

                    String[] listaResiduo = line[8].split(",");
                    List<Residuo> tiposResiduos = Stream.of(listaResiduo)
                        .map(String::trim)
                        .map(residuoNome -> residuoRepo.findByTipo(residuoNome))
                        .filter(r -> r != null)
                        .collect(Collectors.toList());

                    PontoColeta pontoColeta = new PontoColeta();
                    pontoColeta.setNome(nome);
                    pontoColeta.setResponsavel(line[3]);
                    pontoColeta.setTelefoneResponsavel(line[4]);
                    pontoColeta.setEmailResponsavel(line[5]);
                    pontoColeta.setEndereco(line[6]);
                    pontoColeta.setHorario(line[7]);
                    pontoColeta.setBairro(bairro);
                    pontoColeta.setTiposResiduos(tiposResiduos);
                    pontoColetaRepo.save(pontoColeta);

                    System.out.println("Ponto de coleta adicionado: " + nome);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

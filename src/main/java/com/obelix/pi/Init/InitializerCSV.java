package com.obelix.pi.Init;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.obelix.pi.model.Bairro;
import com.obelix.pi.model.PontoColeta;
import com.obelix.pi.model.Residuo;
import com.obelix.pi.model.Rua;
import com.obelix.pi.repository.BairroRepo;
import com.obelix.pi.repository.PontoColetaRepo;
import com.obelix.pi.repository.RuaRepo;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;

import java.nio.file.*;
import java.io.*;
import java.util.List;
import java.util.stream.*;
import org.springframework.beans.factory.annotation.Autowired;

@Component
public class InitializerCSV implements CommandLineRunner {

    // Exemplo de injeção de um repositório JPA
    @Autowired
    private BairroRepo bairroRepo;

    @Autowired
    private PontoColetaRepo pontoColetaRepo;

    @Autowired
    private RuaRepo ruaRepo;

    @Override
    public void run(String... args) throws Exception {
        importarBairrosCSV();
        importarPontoColetaCSV();
    }

    private void importarBairrosCSV() throws CsvValidationException {
        // Lógica para ler o CSV, validar e salvar no banco
        // Exemplo:
        // 1. Ler linhas do CSV
        // 2. Para cada linha, verificar se já existe no banco
        // 3. Se não existir, salvar

        // Exemplo de leitura:
        try (CSVReader reader = new CSVReader(new FileReader("src/main/resources/data/bairros.csv"))) {
            String[] line;
            boolean isHeader = true;
            while ((line = reader.readNext()) != null) {
                if (isHeader) {
                    isHeader = false; // Ignora o cabeçalho
                    continue;
                }
                Long bairroId = Long.parseLong(line[0]);
                String bairroNome = line[1];

                if(bairroRepo.existsById(bairroId)) {
                    System.out.println("Bairro já existe: " + bairroNome);
                } else {
                    // Cria e salva o novo bairro
                    Bairro bairro = new Bairro();
                    bairro.setId(bairroId);
                    bairro.setNome(bairroNome);
                    bairroRepo.save(bairro);
                    System.out.println("Bairro adicionado: " + bairroNome);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void importarPontoColetaCSV() throws CsvValidationException {
        // Lógica para ler o CSV, validar e salvar no banco
        // Exemplo:
        // 1. Ler linhas do CSV
        // 2. Para cada linha, verificar se já existe no banco
        // 3. Se não existir, salvar

        // Exemplo de leitura:
        try (CSVReader reader = new CSVReader(new FileReader("src/main/resources/data/pontos_coleta.csv"))) {
            String[] line;
            boolean isHeader = true;
            while ((line = reader.readNext()) != null) {
                if (isHeader) {
                    isHeader = false; // Ignora o cabeçalho
                    continue;
                }
                Long pontoColetaId = Long.parseLong(line[0]);
                String nome = line[2];
                String responsavel = line[3];
                String telefoneResponsavel = line[4];
                String emailResponsavel = line[5];
                String endereco = line[6];
                String horario = line[7];

                Bairro bairro = bairroRepo.findById(Long.parseLong(line[1])).orElse(null);

                String[] listaResiduo = line[8].split(",");
                List<Residuo> tiposResiduos = Stream.of(listaResiduo)
                        .map(String::trim)
                        .map(residuoNome -> {
                            Residuo residuo = new Residuo();
                            residuo.setTipo(residuoNome);
                            return residuo;
                        })
                        .collect(Collectors.toList());

                // if(bairroRepo.existsById(bairroId)) {
                //     System.out.println("Bairro já existe: " + bairroNome);
                // } else {
                //     // Cria e salva o novo bairro
                //     Bairro bairro = new Bairro();
                //     bairro.setId(bairroId);
                //     bairro.setNome(bairroNome);
                //     bairroRepo.save(bairro);
                //     System.out.println("Bairro adicionado: " + bairroNome);
                // }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

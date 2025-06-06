package com.obelix.pi.repository;

import com.opencsv.CSVReader;
import com.obelix.pi.model.Usuario;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Component;

import java.io.FileReader;

@Component
public class DataInitializer {

    @PersistenceContext
    private EntityManager em;

    @PostConstruct
    public void init() {
        try {
            em.getTransaction().begin();

            importarUsuarios();

            em.getTransaction().commit();
            System.out.println("Importação concluída com sucesso.");
        } catch (Exception e) {
            e.printStackTrace();
            em.getTransaction().rollback();
        }
    }

    private void importarUsuarios() throws Exception {
        try (CSVReader reader = new CSVReader(new FileReader("usuarios.csv"))) {
            reader.readNext(); // Pula cabeçalho
            String[] linha;
            while ((linha = reader.readNext()) != null) {
                String nome = linha[0];
                String email = linha[1];
                String senha = linha[2];
                String perfil = linha[3];

                Usuario usuario = new Usuario(null, nome, email, senha, perfil);
                em.persist(usuario);
            }
        }
    }
}

package com.obelix.pi.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

@Entity
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String email;
    private String senhaHash;
    private String perfil;

    public Usuario() {
    }

    public Usuario(Long id, String nome, String email, String senhaHash, String perfil) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.senhaHash = transformarSenhaEmHash(senhaHash);
        this.perfil = perfil;
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public String getSenhaHash() {
        return senhaHash;
    }

    public String getPerfil() {
        return perfil;
    }

    private String transformarSenhaEmHash(String senha) {
        // Implementar lógica de hash de senha aqui (ex: BCrypt)
        return senha; // Placeholder
    }
}


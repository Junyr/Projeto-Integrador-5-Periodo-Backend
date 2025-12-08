package com.obelix.pi.controllers.DTO;

import com.obelix.pi.model.Usuario;

public class UsuarioResponseDTO {

    private String email;
    private String nome ;

    public UsuarioResponseDTO(String email, String nome) {
        this.email = email;
        this.nome = nome;
    }

    public UsuarioResponseDTO(Usuario usuario) {
        this.email = usuario.getEmail();
        this.nome = usuario.getNome();
    }

    public String getEmail() {
        return email;
    }

    public String getNome() {
        return nome;
    }

}

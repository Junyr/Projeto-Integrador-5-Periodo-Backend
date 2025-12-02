package com.obelix.pi.controllers.DTO;

/**
 * DTO para validação/login do usuário.
 */
public class UsuarioRequestDTO {

    private String email;
    private String senha;

    public UsuarioRequestDTO() {}

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }
}

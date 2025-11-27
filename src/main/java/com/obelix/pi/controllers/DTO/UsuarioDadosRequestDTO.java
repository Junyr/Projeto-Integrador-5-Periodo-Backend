package com.obelix.pi.controllers.DTO;

/**
 * DTO para atualizar dados do usuário.
 */
public class UsuarioDadosRequestDTO {
    private String nome;
    private String email;

    // Getters / Setters
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}

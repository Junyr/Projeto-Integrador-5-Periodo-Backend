package com.obelix.pi.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.obelix.pi.controllers.DTO.UsuarioDadosRequestDTO;
import com.obelix.pi.controllers.DTO.UsuarioRequestDTO;
import com.obelix.pi.model.Usuario;
import com.obelix.pi.repository.UsuarioRepo;

/**
 * Controller de Usuário. Removi o uso de getReferenceByEmail aqui para usar findByEmail.
 */
@RestController
@RequestMapping("/usuario")
public class UsuarioController {

    @Autowired
    private UsuarioRepo repo;

    @PostMapping("/validar")
    public ResponseEntity<Boolean> validarUsuario(@RequestBody UsuarioRequestDTO requestDTO) {
        if (requestDTO.getEmail() == null || requestDTO.getSenha() == null ||
            requestDTO.getEmail().isBlank() || requestDTO.getSenha().isBlank()) {
            throw new RuntimeException("Por favor preencha todos os campos obrigatórios: email e senha.");
        }

        if (!requestDTO.getEmail().matches("^.+@(gmail\\.com|hotmail\\.com|outlook\\.(com|com\\.br))$")) {
            throw new RuntimeException("Por favor, forneça um email válido.");
        }
        if (requestDTO.getSenha().length() < 6) {
            throw new RuntimeException("A senha deve ter pelo menos 6 caracteres.");
        }

        if (!repo.existsByEmail(requestDTO.getEmail())) throw new RuntimeException("Usuário não encontrado.");

        Usuario usuario = repo.findByEmail(requestDTO.getEmail()).orElseThrow(() -> new RuntimeException("Usuário não encontrado."));
        if (!usuario.validarSenha(requestDTO.getSenha(), usuario.getSenha())) throw new RuntimeException("Senha incorreta.");

        return ResponseEntity.ok(true);
    }

    @PostMapping("/adicionar")
    public ResponseEntity<Void> cadastrar(@RequestBody Usuario usuario) {
        if (usuario.getNome() == null || usuario.getEmail() == null || usuario.getSenha() == null ||
            usuario.getNome().isBlank() || usuario.getEmail().isBlank() || usuario.getSenha().isBlank()) {
            throw new RuntimeException("Por favor preencha todos os campos obrigatórios: nome, email e senha.");
        }
        if (!usuario.getNome().matches("^[a-zA-Zà-úÀ-Úâ-ûÂ-ÛçÇ]+(\\s[a-zA-Zà-úÀ-Úâ-ûÂ-ÛçÇ]+)*$")) {
            throw new RuntimeException("Forneça um nome válido, apenas letras e espaços são permitidos.");
        }
        if (!usuario.getEmail().matches("^.+@(gmail\\.com|hotmail\\.com|outlook\\.(com|com\\.br))$")) {
            throw new RuntimeException("Por favor, forneça um email válido.");
        }
        if (usuario.getSenha().length() < 6) throw new RuntimeException("A senha deve ter pelo menos 6 caracteres.");

        usuario.transformarSenhaEmHash();
        repo.save(usuario);
        return ResponseEntity.status(201).build();
    }

    @PutMapping("/atualizar/dados/{id}")
    public ResponseEntity<Void> atualizarDados(@PathVariable Long id, @RequestBody UsuarioDadosRequestDTO requestDTO) {
        if (requestDTO.getNome() == null || requestDTO.getEmail() == null ||
            requestDTO.getNome().isBlank() || requestDTO.getEmail().isBlank()) {
            throw new RuntimeException("Por favor preencha todos os campos obrigatórios: nome, email.");
        }
        if (!repo.existsById(id)) throw new RuntimeException("Usuário não encontrado.");
        if (!requestDTO.getNome().matches("^[a-zA-Zà-úÀ-Úâ-ûÂ-ÛçÇ]+(\\s[a-zA-Zà-úÀ-Úâ-ûÂ-ÛçÇ]+)*$")) {
            throw new RuntimeException("Forneça um nome válido, apenas letras e espaços são permitidos.");
        }
        if (!requestDTO.getEmail().matches("^.+@(gmail\\.com|hotmail\\.com|outlook\\.(com|com\\.br))$")) {
            throw new RuntimeException("Por favor, forneça um email válido.");
        }
        Usuario usuario = repo.findById(id).orElseThrow(() -> new RuntimeException("Usuário não encontrado."));
        usuario.setNome(requestDTO.getNome());
        usuario.setEmail(requestDTO.getEmail());
        repo.save(usuario);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/atualizar/senha/{id}")
    public ResponseEntity<Void> atualizarSenha(@PathVariable Long id, @RequestBody String novaSenha) {
        if (!repo.existsById(id)) throw new RuntimeException("Usuário não encontrado");
        if (novaSenha == null || novaSenha.length() < 6) throw new RuntimeException("A senha deve ter pelo menos 6 caracteres.");
        Usuario usuario = repo.findById(id).orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        usuario.setSenha(novaSenha);
        usuario.transformarSenhaEmHash();
        repo.save(usuario);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/deletar/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        if (!repo.existsById(id)) return ResponseEntity.notFound().build();
        repo.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

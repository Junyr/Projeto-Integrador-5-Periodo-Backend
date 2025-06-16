package com.obelix.pi.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.obelix.pi.controllers.DTO.UsuarioDadosRequestDTO;
import com.obelix.pi.controllers.DTO.UsuarioRequestDTO;
import com.obelix.pi.model.Usuario;
import com.obelix.pi.repository.UsuarioRepo;

@RestController
@RequestMapping("/usuario")
public class UsuarioController {

    @Autowired
    UsuarioRepo repo;

    @PostMapping("/validar")
    public boolean validarUsuario(@RequestBody UsuarioRequestDTO requestDTO) {
        if (requestDTO.getEmail() == null || requestDTO.getSenha() == null || requestDTO.getEmail().isEmpty() || requestDTO.getSenha().isEmpty()) {
            throw new RuntimeException("Por favor preencha todos os campos obrigatórios: email e senha.");
        }

        if (requestDTO.getEmail().matches("^.+@(gmail\\.com|hotmail\\.com|outlook\\.(com|com\\.br))$")) {
            if (requestDTO.getSenha().length() >= 6) {
                if(repo.existsByEmail(requestDTO.getEmail())){
                    Usuario usuario = repo.getReferenceByEmail(requestDTO.getEmail());
                    if(usuario.validarSenha(requestDTO.getSenha(), usuario.getSenha())) {
                        return true;
                    } else throw new RuntimeException("Senha incorreta.");
                }  else throw new RuntimeException("Usuário não encontrado.");
            } else throw new RuntimeException("A senha deve ter pelo menos 6 caracteres.");
        } else throw new RuntimeException("Por favor, forneça um email válido.");
    }

    @PostMapping("/adicionar")
    public void cadastrar(@RequestBody Usuario usuario) {
        if(usuario.getNome() == null || usuario.getEmail() == null || usuario.getNome().isEmpty() || usuario.getEmail().isEmpty() ||
        usuario.getSenha() == null || usuario.getSenha().isEmpty()) {
            throw new RuntimeException("Por favor preencha todos os campos obrigatórios: nome, email e senha.");
        }

        if(usuario.getNome().matches("^[a-zA-Zà-úÀ-Úâ-ûÂ-ÛçÇ]+(\\s[a-zA-Zà-úÀ-Úâ-ûÂ-ÛçÇ]+)?$")) {
            if(usuario.getEmail().matches("^.+@(gmail\\.com|hotmail\\.com|outlook\\.(com|com\\.br))$")) {
                if(usuario.getSenha().length() >= 6) {
                    usuario.transformarSenhaEmHash();
                    repo.save(usuario);
                } else throw new RuntimeException("A senha deve ter pelo menos 6 caracteres.");
            } else throw new RuntimeException("Por favor, forneça um email válido.");
        } else throw new RuntimeException("Forneça um nome válido, apenas letras e espaços são permitidos.");
        
    }

    @PutMapping("/atualizar/dados/{id}")
    public void atualizar(@PathVariable Long id, @RequestBody UsuarioDadosRequestDTO requestDTO) {
        if(requestDTO.getNome() == null || requestDTO.getEmail() == null || requestDTO.getNome().isEmpty() || requestDTO.getEmail().isEmpty()) {
            throw new RuntimeException("Por favor preencha todos os campos obrigatórios: nome, email e senha.");
        }
        
        if (repo.existsById(id)) {
            if(requestDTO.getNome().matches("^[a-zA-Zà-úÀ-Úâ-ûÂ-ÛçÇ]+(\\s[a-zA-Zà-úÀ-Úâ-ûÂ-ÛçÇ]+)?$")) {
                if(requestDTO.getEmail().matches("^.+@(gmail\\.com|hotmail\\.com|outlook\\.(com|com\\.br))$")) {
                    Usuario usuario = repo.getReferenceById(id);
                    usuario.setNome(requestDTO.getNome());
                    usuario.setEmail(requestDTO.getEmail());
                    repo.save(usuario);
                } else throw new RuntimeException("Por favor, forneça um email válido.");
            } else throw new RuntimeException("Forneça um nome válido, apenas letras e espaços são permitidos.");
        } else throw new RuntimeException("Usuario não encontrado.");
    }

    @PutMapping("/atualizar/senha/{id}")
    public void atualizar(@PathVariable Long id, @RequestBody String novaSenha) {
        if (repo.existsById(id)) {
            if(novaSenha.length() >= 6) {
                Usuario usuario = repo.getReferenceById(id);
                usuario.setSenha(novaSenha);
                usuario.transformarSenhaEmHash();
                repo.save(usuario);
            } else throw new RuntimeException("A senha deve ter pelo menos 6 caracteres.");
        } else throw new RuntimeException("Usuário não encontrado");
    }

    @DeleteMapping("/deletar/{id}")
    public void deletar(@PathVariable Long id) {
        repo.deleteById(id);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeException(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

}


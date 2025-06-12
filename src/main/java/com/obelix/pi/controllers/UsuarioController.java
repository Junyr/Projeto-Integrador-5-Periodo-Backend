package com.obelix.pi.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.obelix.pi.controllers.DTO.UsuarioRequestDTO;
import com.obelix.pi.model.Rua;
import com.obelix.pi.model.Usuario;
import com.obelix.pi.repository.UsuarioRepo;
import com.obelix.pi.service.interfaces.IUsuarioService;

@RestController
@RequestMapping("/usuario")
public class UsuarioController {

    @Autowired
    UsuarioRepo repo;

    @GetMapping("/listar")
    public List<Usuario> listar() {
        return repo.findAll();
    }

    @PostMapping("/adicionar")
    public void cadastrar(@RequestBody Usuario usuario) {
        if(usuario.getNome() == null || usuario.getEmail() == null || usuario.getNome().isEmpty() || usuario.getEmail().isEmpty() ||
        usuario.getSenhaHash() == null || usuario.getSenhaHash().isEmpty()) {
            if(usuario.getNome().matches("^[a-zA-Zà-úÀ-Úâ-ûÂ-ÛçÇ]+(\\s[a-zA-Zà-úÀ-Úâ-ûÂ-ÛçÇ]+)?$")) {
                if(usuario.getEmail().matches("^.+@(gmail\\.com|hotmail\\.com|outlook\\.(com|com\\.br))$")) {
                    if(usuario.getSenhaHash().length() >= 6) {
                        usuario.transformarSenhaEmHash();
                        repo.save(usuario);
                    } else throw new RuntimeException("A senha deve ter pelo menos 6 caracteres.");
                } else throw new RuntimeException("Por favor, forneça um email válido.");
            } else throw new RuntimeException("Forneça um nome válido, apenas letras e espaços são permitidos.");
        } else throw new RuntimeException("Por favor preencha todos os campos obrigatórios: nome, email e senha.");
    }

    @PutMapping("/atualizar/dados/{id}")
    public void atualizar(@PathVariable Long id, @RequestBody UsuarioRequestDTO requestDTO) {
        if (repo.existsById(id)) {
            Usuario atualizarUsuario = repo.getReferenceById(id);
            repo.save(atualizarUsuario);
        } else {
            throw new RuntimeException("Rua não encontrada");
        }
    }

    @PutMapping("/atualizar/senha/{id}")
    public void atualizar(@PathVariable Long id, @RequestBody String novaSenha) {
        if (repo.existsById(id)) {
            if(novaSenha.length() >= 6) {
                Usuario usuario = repo.getReferenceById(id);
                usuario.setSenhaHash(novaSenha);
                usuario.transformarSenhaEmHash();
                repo.save(usuario);
            } else throw new RuntimeException("A senha deve ter pelo menos 6 caracteres.");
        } else throw new RuntimeException("Usuário não encontrado");
    }

    @DeleteMapping("/deletar/{id}")
    public void deletar(@PathVariable Long id) {
        repo.deleteById(id);
    }

}


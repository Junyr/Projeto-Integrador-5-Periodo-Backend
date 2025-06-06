package com.obelix.pi.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.obelix.pi.model.Caminhao;
import com.obelix.pi.service.interfaces.ICaminhaoService;

@RestController
@RequestMapping("/api/caminhoes")
public class CaminhaoController {

    private final ICaminhaoService caminhaoService;

    public CaminhaoController(ICaminhaoService caminhaoService) {
        this.caminhaoService = caminhaoService;
    }

    @PostMapping
    public ResponseEntity<Void> cadastrar(@RequestBody Caminhao caminhao) {
        caminhaoService.cadastrarCaminhao(caminhao);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    public ResponseEntity<List<Caminhao>> listar() {
        return ResponseEntity.ok(caminhaoService.listarCaminhao());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Caminhao> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(caminhaoService.buscarCaminhao(id, null)); // ajuste conforme parâmetro correto
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> atualizar(@PathVariable Long id, @RequestBody Caminhao caminhao) {
        caminhaoService.atualizarCaminhao(id, caminhao);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        caminhaoService.deletarCaminhao(id);
        return ResponseEntity.noContent().build();
    }
}

package com.obelix.pi.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.obelix.pi.model.Itinerario;
import com.obelix.pi.service.interfaces.IItinerarioService;

@RestController
@RequestMapping("/api/itinerarios")
public class ItinerarioController {

    private final IItinerarioService itinerarioService;

    public ItinerarioController(IItinerarioService itinerarioService) {
        this.itinerarioService = itinerarioService;
    }

    @GetMapping("/mensal")
    public ResponseEntity<List<Itinerario>> listarMensal() {
        return ResponseEntity.ok(itinerarioService.listarItinerarioMensal());
    }
}


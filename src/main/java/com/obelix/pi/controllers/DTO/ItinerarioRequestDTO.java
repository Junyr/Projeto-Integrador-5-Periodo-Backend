package com.obelix.pi.controllers.DTO;

import java.time.LocalDate;

import com.obelix.pi.model.Itinerario;

public class ItinerarioRequestDTO {
    private Long id;
    private LocalDate data;
    private Long rotaId;

    public ItinerarioRequestDTO() {}

    public ItinerarioRequestDTO(Itinerario itinerario) {
        if (itinerario == null) return;
        this.id = itinerario.getId();
        this.data = itinerario.getData();
        this.rotaId = itinerario.getRota() != null ? itinerario.getRota().getId() : null;
    }

    // Getters/Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDate getData() { return data; }
    public void setData(LocalDate data) { this.data = data; }

    public Long getRotaId() { return rotaId; }
    public void setRotaId(Long rotaId) { this.rotaId = rotaId; }
}

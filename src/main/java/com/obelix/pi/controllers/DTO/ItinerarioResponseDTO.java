package com.obelix.pi.controllers.DTO;

import java.time.LocalDate;

import com.obelix.pi.model.Itinerario;

import lombok.Data;

@Data
public class ItinerarioResponseDTO {
    private Long id;
    private LocalDate data;
    private Long rotaId;

    public ItinerarioResponseDTO(Itinerario itinerario){
        this.id = itinerario.getId();
        this.data = itinerario.getData();
        this.rotaId = itinerario.getRota().getId();
    }

}

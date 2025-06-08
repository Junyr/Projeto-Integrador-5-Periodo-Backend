package com.obelix.pi.repository;

import java.time.LocalDate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.obelix.pi.model.Itinerario;

@RepositoryRestResource
public interface ItinerarioRepo extends JpaRepository<Itinerario, Long> {
    boolean existsByDataAndRota_Caminhao_Id(LocalDate data, Long caminhaoId);
}

package com.obelix.pi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.webmvc.RepositoryRestController;

import com.obelix.pi.model.Itinerario;

@RepositoryRestController
public interface ItinerarioRepo extends JpaRepository<Itinerario, Long> {
}

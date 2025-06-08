package com.obelix.pi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.webmvc.RepositoryRestController;

import com.obelix.pi.model.Rota;

@RepositoryRestController
public interface RotaRepo extends JpaRepository<Rota, Long> {
}

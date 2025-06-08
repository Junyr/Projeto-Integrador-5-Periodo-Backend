package com.obelix.pi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.obelix.pi.model.Rota;

@RepositoryRestResource
public interface RotaRepo extends JpaRepository<Rota, Long> {
}

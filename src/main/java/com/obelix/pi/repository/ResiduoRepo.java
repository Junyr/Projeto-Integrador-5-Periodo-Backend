package com.obelix.pi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.obelix.pi.model.Residuo;

@RepositoryRestResource
public interface ResiduoRepo extends JpaRepository<Residuo, Long> {
    Residuo findByTipo(String tipo);
    boolean existsByTipo(String tipo);
}

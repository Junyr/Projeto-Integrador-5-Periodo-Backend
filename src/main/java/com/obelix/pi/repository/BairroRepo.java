package com.obelix.pi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import com.obelix.pi.model.Bairro;

@RepositoryRestResource
public interface BairroRepo extends JpaRepository<Bairro, Long> {
    boolean existsByNome(String nome); // Necessário caso Controller / DTO use
    Bairro findByNome(String nome);    // Útil para Services
}

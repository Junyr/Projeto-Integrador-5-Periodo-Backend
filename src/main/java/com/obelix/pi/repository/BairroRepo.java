package com.obelix.pi.repository;

import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.jpa.repository.JpaRepository;
import com.obelix.pi.model.Bairro;

@RepositoryRestResource
public interface BairroRepo extends JpaRepository<Bairro, Long> {
}
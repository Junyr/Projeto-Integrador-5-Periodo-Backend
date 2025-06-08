package com.obelix.pi.repository;

import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.data.jpa.repository.JpaRepository;
import com.obelix.pi.model.Bairro;

@RepositoryRestController
public interface BairroRepo extends JpaRepository<Bairro, Long> {
}
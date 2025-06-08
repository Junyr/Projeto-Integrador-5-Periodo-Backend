package com.obelix.pi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.webmvc.RepositoryRestController;

import com.obelix.pi.model.PontoColeta;

@RepositoryRestController
public interface PontoColetaRepo extends JpaRepository<PontoColeta, Long> {
}

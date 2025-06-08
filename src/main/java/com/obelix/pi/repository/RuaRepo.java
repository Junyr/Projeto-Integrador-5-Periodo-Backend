package com.obelix.pi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.obelix.pi.model.Rua;

@RepositoryRestResource
public interface RuaRepo extends JpaRepository<Rua, Long> {
    Rua findByBairroOrigemAndBairroDestino(Long bairroOrigemId, Long bairroDestinoId);
}
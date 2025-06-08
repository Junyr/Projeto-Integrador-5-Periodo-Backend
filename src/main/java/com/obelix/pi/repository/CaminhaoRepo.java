package com.obelix.pi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.obelix.pi.model.Caminhao;

@RepositoryRestResource
public interface CaminhaoRepo extends JpaRepository<Caminhao, Long> {
    List<Caminhao> findByTipoResiduoId(Long tipoResiduoId);
}

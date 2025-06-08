package com.obelix.pi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.obelix.pi.model.PontoColeta;

@RepositoryRestResource
public interface PontoColetaRepo extends JpaRepository<PontoColeta, Long> {
        List<PontoColeta> findByTipoResiduoId(Long tipoResiduoId);
}

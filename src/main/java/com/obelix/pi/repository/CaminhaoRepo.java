package com.obelix.pi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.obelix.pi.model.Caminhao;

@RepositoryRestResource
public interface CaminhaoRepo extends JpaRepository<Caminhao, Long> {
    @Query("SELECT c FROM Caminhao c JOIN c.tiposResiduos r WHERE r.id = :residuoId")
    List<Caminhao> findByTiposResiduos(@Param("residuoId") Long tipoResiduoId);
}

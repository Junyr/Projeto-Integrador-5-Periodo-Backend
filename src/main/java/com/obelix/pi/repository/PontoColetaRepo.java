package com.obelix.pi.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.obelix.pi.model.PontoColeta;

@RepositoryRestResource
public interface PontoColetaRepo extends JpaRepository<PontoColeta, Long> {

    boolean existsByNome(String nome);

    @Query("SELECT p FROM PontoColeta p JOIN p.tiposResiduos r WHERE r.id = :residuoId")
    List<PontoColeta> findByTiposResiduos(@Param("residuoId") Long tipoResiduoId);

    List<PontoColeta> findByBairro_Id(Long bairroId);
}

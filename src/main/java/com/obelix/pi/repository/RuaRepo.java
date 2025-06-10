package com.obelix.pi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.obelix.pi.model.Rua;

@RepositoryRestResource
public interface RuaRepo extends JpaRepository<Rua, Long> {
    @Query("select r from Rua r where r.origem.id = :origemId and r.destino.id = :destinoId")
    Rua findByOrigemAndDestino(@Param("origemId") Long bairroOrigemId, @Param("destinoId") Long bairroDestinoId);
}
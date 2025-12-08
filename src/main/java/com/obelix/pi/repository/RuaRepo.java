package com.obelix.pi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.obelix.pi.model.Rua;

@RepositoryRestResource
public interface RuaRepo extends JpaRepository<Rua, Long> {

    @Query("SELECT r FROM Rua r WHERE r.origem.id = :origemId AND r.destino.id = :destinoId")
    List<Rua> findByOrigemAndDestino(@Param("origemId") Long origemId,
                                     @Param("destinoId") Long destinoId);

    boolean existsByOrigem_IdAndDestino_Id(Long origemId, Long destinoId);
}

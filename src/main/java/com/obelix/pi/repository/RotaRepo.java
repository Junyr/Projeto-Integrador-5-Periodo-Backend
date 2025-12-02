package com.obelix.pi.repository;

import java.util.Optional;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.obelix.pi.model.Rota;

@RepositoryRestResource
public interface RotaRepo extends JpaRepository<Rota, Long> {

    Optional<Rota> findByOrigem_IdAndDestino_Id(Long origemId, Long destinoId);

    List<Rota> findByCaminhao_Id(Long caminhaoId);

    boolean existsByOrigem_IdAndDestino_Id(Long origemId, Long destinoId);
}

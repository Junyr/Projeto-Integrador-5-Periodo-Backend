package com.obelix.pi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.obelix.pi.model.Usuario;

@RepositoryRestResource
public interface UsuarioRepo extends JpaRepository<Usuario, Long> {
}

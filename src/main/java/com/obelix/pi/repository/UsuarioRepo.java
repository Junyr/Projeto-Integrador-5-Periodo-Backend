package com.obelix.pi.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.obelix.pi.model.Usuario;

@RepositoryRestResource
public interface UsuarioRepo extends JpaRepository<Usuario, Long> {

    boolean existsByEmail(String email);

    // Retorna usuário por email, encapsulado em Optional, para funcionar com orElseThrow()
    Optional<Usuario> findByEmail(String email);

    // Apenas se realmente precisar de referência JPA (não costuma ser necessário)
    Usuario getReferenceByEmail(String email);
}

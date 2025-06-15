package org.example.aces_api.model.repository;

import org.example.aces_api.model.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    Optional<Usuario> findByCpf(String cpf);

    Optional<Usuario> findByEmail(String email);

    List<Usuario> findByNomeContainingIgnoreCase(String nome);

    List<Usuario> findByCargo(String cargo);

    List<Usuario> findByAtivo(boolean ativo);

    boolean existsByCpf(String cpf);

    boolean existsByEmail(String email);
}

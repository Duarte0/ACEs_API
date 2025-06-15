package org.example.aces_api.model.repository;

import org.example.aces_api.model.entity.Agente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AgenteRepository extends JpaRepository<Agente, Integer> {

    Optional<Agente> findByMatricula(String matricula);

    @Query("SELECT a FROM Agente a JOIN a.usuario u WHERE LOWER(u.nome) LIKE LOWER(concat('%', :nome, '%'))")
    List<Agente> findByNomeContaining(@Param("nome") String nome);

    List<Agente> findByAtivo(boolean ativo);

    boolean existsByMatricula(String matricula);

    boolean existsByUsuarioId(Integer usuarioId);

    @Query("SELECT a FROM Agente a JOIN a.areas aa WHERE aa.area.id = :areaId")
    List<Agente> findByAreaId(@Param("areaId") Integer areaId);

    Optional<Agente> findByUsuarioId(Integer usuarioId);
}
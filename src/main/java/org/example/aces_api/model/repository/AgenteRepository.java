package org.example.aces_api.model.repository;

import org.example.aces_api.model.entity.Agente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface AgenteRepository extends JpaRepository<Agente, Integer> {

    Optional<Agente> findByMatricula(String matricula);

    List<Agente> findByAtivo(boolean ativo);

    boolean existsByMatricula(String matricula);

    @Query("SELECT a FROM Agente a JOIN a.areas aa WHERE aa.area.id = :areaId")

    List<Agente> findByAreaId(@Param("areaId") Integer areaId);

    List<Agente> findByNomeContainingIgnoreCase(String nome);
}
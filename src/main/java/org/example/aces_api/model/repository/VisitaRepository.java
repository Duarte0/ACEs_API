package org.example.aces_api.model.repository;

import org.example.aces_api.model.entity.Area;
import org.example.aces_api.model.entity.Visita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface VisitaRepository extends JpaRepository<Visita, Long> {

    List<Visita> findByAgenteId(Long agenteId);

    List<Visita> findByEnderecoId(Long enderecoId);

    List<Visita> findByDataHoraBetween(LocalDateTime inicio, LocalDateTime fim);

    List<Visita> findByStatus(String status);

    List<Visita> findByFoiRealizada(boolean foiRealizada);

    @Query("SELECT COUNT(v) FROM Visita v WHERE v.endereco.area.id = :areaId AND v.dataHora BETWEEN :inicio AND :fim")
    int countVisitasByAreaAndPeriod(Long areaId, LocalDateTime inicio, LocalDateTime fim);
}

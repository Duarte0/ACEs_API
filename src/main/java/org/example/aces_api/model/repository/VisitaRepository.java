package org.example.aces_api.model.repository;

import org.example.aces_api.model.entity.Area;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface VisitaRepository extends JpaRepository<Area, Integer> {

    @Query("SELECT COUNT(v) FROM Visita v WHERE v.endereco.area.id = :areaId AND v.dataHora BETWEEN :inicio AND :fim")
    int countVisitasByAreaAndPeriod(Long areaId, LocalDateTime inicio, LocalDateTime fim);
}

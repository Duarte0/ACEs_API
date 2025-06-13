package org.example.aces_api.model.repository;

import org.example.aces_api.model.entity.Area;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface FocoAedesRepository extends JpaRepository<Area, Long> {

    @Query("SELECT COUNT(f) FROM FocoAedes f WHERE f.visita.endereco.area.id = :areaId " +
            "AND f.visita.dataHora BETWEEN :inicio AND :fim")
    int countFocosByAreaAndPeriod(Long areaId, LocalDateTime inicio, LocalDateTime fim);
}

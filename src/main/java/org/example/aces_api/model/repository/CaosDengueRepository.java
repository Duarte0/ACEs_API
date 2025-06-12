package org.example.aces_api.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface CaosDengueRepository extends JpaRepository<CasoDengue, Long> {

    @Query("SELECT COUNT(c) FROM CasoDengue c WHERE c.visita.endereco.area.id = :areaId AND c.visita.dataHora BETWEEN :inicio AND :fim")
    int countDengueByAreaAndPeriod(Long areaId, LocalDateTime dataGeracao, LocalDateTime fim);
}

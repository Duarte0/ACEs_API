package org.example.aces_api.model.repository;

import org.example.aces_api.model.entity.Area;
import org.example.aces_api.model.entity.CasoDengue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CasoDengueRepository extends JpaRepository<CasoDengue, Long> {
    List<CasoDengue> findByPacienteId(Long pacienteId);

    List<CasoDengue> findByVisitaId(Long visitaId);

    List<CasoDengue> findByDataDiagnosticoBetween(LocalDate inicio, LocalDate fim);

    List<CasoDengue> findByTipoDengue(String tipoDengue);

    List<CasoDengue> findByGravidade(String gravidade);

    List<CasoDengue> findByConfirmadoLab(boolean confirmadoLab);

    @Query("SELECT COUNT(c) FROM CasoDengue c WHERE c.visita.endereco.area.id = :areaId " +
            "AND c.visita.dataHora BETWEEN :inicio AND :fim")
    int countDengueByAreaAndPeriod(
            @Param("areaId") Long areaId,
            @Param("inicio") LocalDateTime dataGeracao, // <--- CORRIGIDO: Mapeia 'dataGeracao' para ':inicio'
            @Param("fim") LocalDateTime fim            // <--- CORRIGIDO: Mapeia 'fim' para ':fim'
    );
}

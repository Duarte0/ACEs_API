package org.example.aces_api.model.repository;

import org.example.aces_api.model.entity.AgenteArea;
import org.example.aces_api.model.entity.AgenteAreaId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AgenteAreaRepository extends JpaRepository<AgenteArea, AgenteAreaId> {
    boolean existsById(AgenteAreaId id);
    List<AgenteArea> findByAgenteId(Integer agenteId);
}

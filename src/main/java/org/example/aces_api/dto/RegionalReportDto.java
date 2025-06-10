package org.example.aces_api.dto;

import java.util.List;

public record RegionalReportDto(
        String regiao,
        List<String> niveisRiscoDaRegiao, // Lista de strings dos níveis de risco das áreas desta região
        List<String> prioridadesDaRegiao, // Lista de strings das prioridades das áreas desta região
        Double mediaNivelRiscoRegional, // Média do risco para esta região
        Double mediaPrioridadeRegional, // Média da prioridade para esta região
        Long totalAreasNaRegiao        // Quantidade de áreas nesta região
) {}
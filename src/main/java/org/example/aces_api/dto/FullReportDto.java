package org.example.aces_api.dto;

import java.util.List;

public record FullReportDto(
        List<RegionalReportDto> relatorioPorRegiao,
        Double mediaNivelRiscoGlobal,
        Double mediaPrioridadeGlobal,
        Long totalAreas
) {
}

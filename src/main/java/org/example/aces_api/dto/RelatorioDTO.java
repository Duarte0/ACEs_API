package org.example.aces_api.dto;

import java.time.LocalDateTime;

public record RelatorioDTO(
        Long areaId,
        String nomeArea,
        String periodo,
        long totalCasos,
        long totalVisitas,
        double indiceDengue,
        LocalDateTime dataGeracao
) {}

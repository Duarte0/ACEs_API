package org.example.aces_api.dto;

import java.time.LocalDateTime;

public record RelatorioDTO(
        int areaId,
        String nomeArea,
        String periodo,
        long totalCasos,
        long totalVisitas,
        long totalFocos,
        double indiceDengue,
        LocalDateTime dataGeracao
) {}

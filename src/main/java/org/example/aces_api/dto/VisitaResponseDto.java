package org.example.aces_api.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record VisitaResponseDto(
        int id,
        int agenteId,
        int enderecoId,
        LocalDateTime dataHora,
        String observacoes,
        String status,
        Boolean foiRealizada
) {

}

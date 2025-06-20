package org.example.aces_api.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record VisitaResponseDto(
        Integer id,
        Integer agenteId,
        Integer enderecoId,
        LocalDateTime dataHora,
        String observacoes,
        String status,
        BigDecimal temperatura,
        Boolean foiRealizada
) {

}

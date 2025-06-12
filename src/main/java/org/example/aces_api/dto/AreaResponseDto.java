package org.example.aces_api.dto;

import java.time.LocalDateTime;

public record AreaResponseDto(
        int id,
        String nome,
        String descricao,
        String regiao,
        int populacaoAprox,
        Risco nivelRisco,
        Risco prioridade,
        LocalDateTime dataUltimaAtt
) {
}

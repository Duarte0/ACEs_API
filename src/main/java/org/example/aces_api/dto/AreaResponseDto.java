package org.example.aces_api.dto;

import java.time.LocalDateTime;

public record AreaResponseDto(
        int id,
        String nome,
        String descricao,
        String regiao,
        int populacaoAprox,
        String nivelRisco,
        String prioridade,
        LocalDateTime dataUltimaAtt
) {
}

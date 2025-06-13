package org.example.aces_api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.time.LocalDateTime;

@JsonPropertyOrder({"id", "nome", "descricao", "regiao", "populacaoAprox", "nivelRisco", "prioridade", "dataUltimaAtt"})
public record AreaResponseDto(
        @JsonProperty("id") // Se quiser que o campo 'id' seja chamado 'id' no JSON, mesmo que o campo interno seja outro (Ex: key)
        Integer id, // Use Integer para compatibilidade com HATEOAS
        String nome,
        String descricao,
        String regiao,
        int populacaoAprox,
        Risco nivelRisco,
        Risco prioridade,
        LocalDateTime dataUltimaAtt
) {
}
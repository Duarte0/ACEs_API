package org.example.aces_api.dto;

public record AgenteUpdateDTO(
        String matricula,
        String dataAdmissao,
        String dataInicio,
        String dataFim,
        Boolean ativo
) {}
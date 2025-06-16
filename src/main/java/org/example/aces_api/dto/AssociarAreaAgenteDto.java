package org.example.aces_api.dto;

import jakarta.validation.constraints.NotNull;

public record AssociarAreaAgenteDto(
        @NotNull(message = "ID do agente é obrigatório")
        Integer agenteId,

        @NotNull(message = "ID da área é obrigatório")
        Integer areaId
) {}
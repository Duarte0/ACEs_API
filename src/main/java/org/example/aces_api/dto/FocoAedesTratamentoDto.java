package org.example.aces_api.dto;

import jakarta.validation.constraints.NotNull;

public record FocoAedesTratamentoDto(
        @NotNull
        boolean tratado,

        String observacoes
) {}


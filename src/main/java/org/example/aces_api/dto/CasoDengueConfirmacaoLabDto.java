package org.example.aces_api.dto;

import jakarta.validation.constraints.NotNull;

public record CasoDengueConfirmacaoLabDto(
        @NotNull
        boolean confirmadoLab,

        String observacoes
) {}
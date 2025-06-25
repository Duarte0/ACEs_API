package org.example.aces_api.dto;


import jakarta.validation.constraints.NotNull;

public record FocoAedesCreateDto(
        @NotNull
        Long visitaId,
        String imagem,
        String tipoFoco,
        @NotNull
        Integer quantidade,
        @NotNull
        Boolean tratado,
        @NotNull
        String observacoes
) {
}

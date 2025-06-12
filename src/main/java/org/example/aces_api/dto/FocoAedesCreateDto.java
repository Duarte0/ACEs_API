package org.example.aces_api.dto;


import jakarta.validation.constraints.NotBlank;

public record FocoAedesCreateDto(
        @NotBlank
        Long visitaId,
        String imagem,
        @NotBlank
        TipoFoco tipoFoco,
        @NotBlank
        Integer quantidade,
        @NotBlank
        Boolean tratado,
        @NotBlank
        String observacoes
) {
}

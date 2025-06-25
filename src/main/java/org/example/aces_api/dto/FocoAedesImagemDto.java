package org.example.aces_api.dto;

import jakarta.validation.constraints.NotBlank;

public record FocoAedesImagemDto(
        @NotBlank
        String imagem
) {}


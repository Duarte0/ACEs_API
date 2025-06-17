package org.example.aces_api.dto;

import jakarta.validation.constraints.NotBlank;

public record VisitaUpdateStatusDto(
        @NotBlank
        String status
) {}

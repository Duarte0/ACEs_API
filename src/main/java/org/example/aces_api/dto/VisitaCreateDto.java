package org.example.aces_api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record VisitaCreateDto(
        @NotNull
        int enderecoId,
        @NotNull
        int agenteId,
        LocalDateTime dataHora,
        @Size(max = 250)
        String observacoes,
        @NotBlank
        @Size(max = 50)
        String status,
        BigDecimal temperatura,
        Boolean foiRealizada
) {}
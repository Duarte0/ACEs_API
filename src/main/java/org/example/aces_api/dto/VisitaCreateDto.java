package org.example.aces_api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record VisitaCreateDto(
        @NotBlank
        Integer enderecoId,
        @PastOrPresent
        LocalDateTime dataHora,
        @Size(max = 100)
        String observacoes,
        @NotBlank
        @Size(max = 50)
        String status,
        BigDecimal temperatura,
        @NotBlank
        Boolean foiRealizada
) {}
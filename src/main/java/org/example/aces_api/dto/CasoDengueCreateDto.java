package org.example.aces_api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record CasoDengueCreateDto(
        @NotNull
        Integer pacienteId,
        @NotNull
        Long visitaId,
        @NotNull
        LocalDate dataDiagnostico,
        @NotNull
        TipoDengue tipoDengue,
        @NotBlank
        String gravidade,
        @NotBlank
        String sintomas,
        @Size(max = 100)
        String observacoes,
        Boolean confirmadoLab
) {
}

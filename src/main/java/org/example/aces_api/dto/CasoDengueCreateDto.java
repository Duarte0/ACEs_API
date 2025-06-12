package org.example.aces_api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record CasoDengueCreateDto(
        @NotBlank
        Integer pacienteId,
        @NotBlank
        LocalDate dataDiagnostico,
        @NotBlank
        Enum tipoDengue,
        @NotBlank
        String gravidade,
        @NotBlank
        String sintomas,
        @Size(max = 100)
        String observacoes,
        @NotBlank
        Boolean confirmadoLab
) {
}

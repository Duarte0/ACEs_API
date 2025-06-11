package org.example.aces_api.dto;

import java.time.LocalDate;

public record CasoDengueResponseDto
        (
                int visitaId,
                int pacienteId,
                LocalDate dataDiagnostico,
                String tipoDengue,
                String gravidade,
                String sintomas,
                String observacoes,
                Boolean confirmadoLab
) {
}

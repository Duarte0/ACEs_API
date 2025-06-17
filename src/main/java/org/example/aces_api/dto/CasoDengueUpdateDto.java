package org.example.aces_api.dto;

import jakarta.validation.constraints.PastOrPresent;
import java.time.LocalDate;

public record CasoDengueUpdateDto(
        @PastOrPresent
        LocalDate dataDiagnostico,

        String tipoDengue,

        String gravidade,

        String sintomas,

        String observacoes
) {}


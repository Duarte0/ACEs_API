package org.example.aces_api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AgenteRequestDTO(

        @NotBlank(message = "Matrícula é obrigatória")
        String matricula,

        String nome,
        String dataAdmissao,
        String dataInicio,
        String dataFim,
        boolean ativo
) {}
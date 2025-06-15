package org.example.aces_api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AgenteRequestDTO(
        @NotNull(message = "ID do usuário é obrigatório")
        Integer usuarioId,

        @NotBlank(message = "Matrícula é obrigatória")
        String matricula,

        String dataAdmissao,
        String dataInicio,
        String dataFim
) {}
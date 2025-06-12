package org.example.aces_api.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record AreaCreateDto(

        @NotBlank(message = "O nome da área é obrigatório")
        @Size(max = 100, message = "O nome não pode ter mais de 100 caracteres")
        String nome,

        String descricao,

        @Size(max = 50, message = "A região não pode ter mais de 50 caracteres")
        String regiao,

        @NotNull(message = "A população aproximada é obrigatória")
        @Min(value = 0, message = "A população aproximada não pode ser negativa")
        int populacaoAprox,

        @NotNull(message = "O nível de risco é obrigatório")
        Risco nivelRisco,

        @NotNull(message = "O nível de risco é obrigatório")
        Risco prioridade
) {
}

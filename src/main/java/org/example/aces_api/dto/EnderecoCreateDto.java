package org.example.aces_api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record EnderecoCreateDto(
        @NotNull(message = "O ID da área é obrigatório")
        Integer area_id,

        @NotBlank(message = "O logradouro é obrigatório")
        @Size(max = 100, message = "O logradouro não pode ter mais de 100 caracteres")
        String logradouro,

        @Size(max = 20, message = "O número não pode ter mais de 20 caracteres")
        String numero,

        @NotBlank(message = "O bairro é obrigatório")
        @Size(max = 50, message = "O bairro não pode ter mais de 50 caracteres")
        String bairro,

        @NotBlank(message = "O CEP é obrigatório")
        @Size(max = 10, message = "O CEP não pode ter mais de 10 caracteres")
        String cep,

        BigDecimal latitude,
        BigDecimal longitude,
        Boolean spolmovel
) {
}
package org.example.aces_api.dto;

import jakarta.validation.constraints.NotBlank;

public record UsuarioRequestDTO(
        @NotBlank String nome,
        @NotBlank String cpf,
        @NotBlank String email,
        String telefone,
        String cargo
) {}
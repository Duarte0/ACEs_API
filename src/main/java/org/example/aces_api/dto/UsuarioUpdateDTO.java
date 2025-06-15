package org.example.aces_api.dto;

public record UsuarioUpdateDTO(
        String nome,
        String telefone,
        String cargo,
        String email
) {
}

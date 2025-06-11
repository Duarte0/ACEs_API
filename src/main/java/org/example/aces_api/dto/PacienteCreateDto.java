package org.example.aces_api.dto;

import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record PacienteCreateDto(

        @NotNull(message = "O ID do endereço é obrigatório")
        int endeco_id,

        @NotBlank(message = "O nome do paciente é obrigatório")
        @Size(max = 100, message = "O nome não pode ter mais de 100 caracteres")
        String nome,

        @PastOrPresent(message = "A data de nascimento não pode ser futura")
        LocalDate dataNascimento,

        @Pattern(regexp = "[MmFfOo]", message = "Sexo inválido. Use 'M', 'F' ou 'O'.")
        Character sexo,

        @Pattern(regexp = "^\\d{11}$", message = "CPF inválido. Use apenas 11 dígitos numéricos.")
        String cpf,

        @Size(max = 20, message = "O Cartão SUS não pode ter mais de 20 caracteres")
        String cartaoSUS,

        @Size(max = 20, message = "O telefone não pode ter mais de 20 caracteres")
        String telefone
) {
}

package org.example.aces_api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.time.LocalDate;

@JsonPropertyOrder({"id", "endeco_id", "nome", "dataNascimento", "sexo", "cpf","cartaoSUS", "telefone"})
public record PacienteResponseDto (
        @JsonProperty("id")
        Integer id,
        Integer endereco_id,
        String nome,
        LocalDate dataNascimento,
        Sexo sexo,
        String cpf,
        String cartaoSUS,
        String telefone
){}

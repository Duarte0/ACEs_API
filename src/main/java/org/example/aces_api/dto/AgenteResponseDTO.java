package org.example.aces_api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.hateoas.Links;

import java.time.LocalDate;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record AgenteResponseDTO(
        Integer id,
        String nome,
        String matricula,
        LocalDate dataAdmissao,
        LocalDate dataInicio,
        LocalDate dataFim,
        Boolean ativo,
        Links _links
) {}
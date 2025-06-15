package org.example.aces_api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.hateoas.Links;

import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record UsuarioResponseDTO(
        Integer id,
        String nome,
        String email,
        String cargo,
        LocalDateTime dataCriacao,
        Links _links


) {
}

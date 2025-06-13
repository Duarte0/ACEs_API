package org.example.aces_api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.math.BigDecimal;

@JsonPropertyOrder({"id", "area_id", "logradouro", "numero", "bairro", "cep", "latitude", "longitude", "spolmovel"})
public record EnderecoResponseDto(
        @JsonProperty("id")
        Integer id,
        Integer area_id,
        String logradouro,
        String numero,
        String bairro,
        String cep,
        BigDecimal latitude,
        BigDecimal longitude,
        Boolean spolmovel
) {
}
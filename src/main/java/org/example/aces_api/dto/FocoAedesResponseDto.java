package org.example.aces_api.dto;

import org.example.aces_api.model.entity.FocoAedes;
import org.example.aces_api.model.entity.Visita;

import java.time.LocalDateTime;

public record FocoAedesResponseDto(
        int id,
        int visitaId,
        String imagem,
        String tipoFoco,
        Integer quantidade,
        Boolean tratado,
        String observacoes,
        LocalDateTime dataVisita
) {

}


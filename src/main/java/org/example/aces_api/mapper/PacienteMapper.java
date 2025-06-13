package org.example.aces_api.mapper;

import org.example.aces_api.dto.PacienteCreateDto;
import org.example.aces_api.dto.PacienteResponseDto;
import org.example.aces_api.model.entity.Paciente;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PacienteMapper extends BaseMapper<Paciente, PacienteResponseDto> {

    Paciente toEntity(PacienteCreateDto dto);

    @Mapping(source = "endereco.id", target = "endereco_id")
    PacienteResponseDto toDto(Paciente entity);
}

package org.example.aces_api.mapper;

import org.example.aces_api.dto.CasoDengueCreateDto;
import org.example.aces_api.dto.CasoDengueResponseDto;
import org.example.aces_api.dto.CasoDengueUpdateDto;
import org.example.aces_api.model.entity.CasoDengue;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CasoDengueMapper extends BaseMapper<CasoDengue, CasoDengueResponseDto> {

    default String map(Enum<?> value) {
        return value != null ? value.name() : null;
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "visita.id", source = "visitaId")
    @Mapping(target = "paciente.id", source = "pacienteId")
    CasoDengue toEntity(CasoDengueCreateDto dto);

    @Mapping(source = "visita.id", target = "visitaId")
    @Mapping(source = "paciente.id", target = "pacienteId")
    CasoDengueResponseDto toDto(CasoDengue entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "visita", ignore = true)
    @Mapping(target = "paciente", ignore = true)
    @Mapping(target = "confirmadoLab", ignore = true)
    void updateEntityFromDto(CasoDengueUpdateDto dto, @MappingTarget CasoDengue entity);
}

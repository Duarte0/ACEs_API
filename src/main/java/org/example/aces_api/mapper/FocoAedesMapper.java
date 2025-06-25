package org.example.aces_api.mapper;

import org.example.aces_api.dto.FocoAedesCreateDto;
import org.example.aces_api.dto.FocoAedesResponseDto;
import org.example.aces_api.model.entity.FocoAedes;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface FocoAedesMapper extends BaseMapper<FocoAedes, FocoAedesResponseDto> {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "visita.id", source = "visitaId")
    FocoAedes toEntity(FocoAedesCreateDto dto);

    @Mapping(source = "visita.id", target = "visitaId")
    FocoAedesResponseDto toDto(FocoAedes entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "visita", ignore = true)
    void updateEntityFromDto(FocoAedesCreateDto dto, @MappingTarget FocoAedes entity);
}


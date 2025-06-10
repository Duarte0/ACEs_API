package org.example.aces_api.mapper;

import org.example.aces_api.dto.AreaCreateDto;
import org.example.aces_api.dto.AreaResponseDto;
import org.example.aces_api.model.entity.Area;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AreaMapper extends BaseMapper<Area, AreaResponseDto> {

    Area toEntity(AreaCreateDto dto);
}

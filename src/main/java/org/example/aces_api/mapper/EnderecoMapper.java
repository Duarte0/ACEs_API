package org.example.aces_api.mapper;

import org.example.aces_api.dto.EnderecoCreateDto;
import org.example.aces_api.dto.EnderecoResponseDto;
import org.example.aces_api.model.entity.Endereco;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface EnderecoMapper extends BaseMapper<Endereco, EnderecoResponseDto> {

    Endereco toEntity(EnderecoCreateDto dto);

    @Mapping(source = "area.id", target = "area_id")
    EnderecoResponseDto toDto(Endereco entity);


}

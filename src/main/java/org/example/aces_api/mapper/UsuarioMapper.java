package org.example.aces_api.mapper;

import org.example.aces_api.dto.UsuarioRequestDTO;
import org.example.aces_api.dto.UsuarioResponseDTO;
import org.example.aces_api.model.entity.Usuario;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "ativo", constant = "true")
    @Mapping(target = "dataCriacao", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "ultimoAcesso", ignore = true)
    Usuario requestToEntity(UsuarioRequestDTO dto);

    @Mapping(target = "_links", ignore = true)
    UsuarioResponseDTO toResponseDTO(Usuario entity);
}

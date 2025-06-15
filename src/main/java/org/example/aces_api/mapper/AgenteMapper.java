package org.example.aces_api.mapper;

import org.example.aces_api.dto.AgenteRequestDTO;
import org.example.aces_api.dto.AgenteResponseDTO;
import org.example.aces_api.model.entity.Agente;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.time.LocalDate;

@Mapper(componentModel = "spring")
public interface AgenteMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dataAdmissao", source = "dataAdmissao", qualifiedByName = "stringToLocalDate")
    @Mapping(target = "dataInicio", source = "dataInicio", qualifiedByName = "stringToLocalDate")
    @Mapping(target = "dataFim", source = "dataFim", qualifiedByName = "stringToLocalDate")
    @Mapping(target = "ativo", constant = "true")
    @Mapping(target = "areas", ignore = true)
    Agente requestToEntity(AgenteRequestDTO dto);

    @Mapping(target = "usuarioId", source = "usuario.id")
    @Mapping(target = "usuarioNome", source = "usuario.nome")
    @Mapping(target = "_links", ignore = true)
    AgenteResponseDTO toResponseDTO(Agente entity);

    // Conversor de String para LocalDate
    @Named("stringToLocalDate")
    default LocalDate stringToLocalDate(String dateString) {
        if (dateString == null || dateString.isEmpty()) {
            return null;
        }
        return LocalDate.parse(dateString);
    }
}
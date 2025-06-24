package org.example.aces_api.mapper;

import org.example.aces_api.dto.AgenteRequestDTO;
import org.example.aces_api.dto.AgenteResponseDTO;
import org.example.aces_api.model.entity.Agente;
import org.mapstruct.*;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Links;

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

    @Mapping(target = "_links", ignore = true)
    AgenteResponseDTO toResponseDTO(Agente entity);

    @Named("stringToLocalDate")
    default LocalDate stringToLocalDate(String dateString) {
        if (dateString == null || dateString.isEmpty()) {
            return null;
        }
        return LocalDate.parse(dateString);
    }

    @AfterMapping
    default void addLinks(@MappingTarget AgenteResponseDTO dto, Agente agente) {
        Links links = Links.of(
                Link.of("/api/agentes/" + agente.getId()).withSelfRel(),
                Link.of("/api/agentes/" + agente.getId() + "/areas").withRel("areas"),
                Link.of("/api/agentes/" + agente.getId() + "/ativar").withRel("ativar").withType("PATCH"),
                Link.of("/api/agentes/" + agente.getId() + "/desativar").withRel("desativar").withType("PATCH")
        );
        dto.withLinks(links);
    }
}

package org.example.aces_api.mapper;

import org.example.aces_api.dto.VisitaCreateDto;
import org.example.aces_api.dto.VisitaResponseDto;
import org.example.aces_api.dto.VisitaUpdateStatusDto;
import org.example.aces_api.model.entity.Visita;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface VisitaMapper extends BaseMapper<Visita, VisitaResponseDto> {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "agente.id", source = "agenteId")
    @Mapping(target = "endereco.id", source = "enderecoId")
    Visita toEntity(VisitaCreateDto dto);

    @Mapping(source = "agente.id", target = "agenteId")
    @Mapping(source = "endereco.id", target = "enderecoId")
    VisitaResponseDto toDto(Visita entity);

    @Named("formatarEndereco")
    default String formatarEndereco(String logradouro) {
        // Implemente a lógica de formatação do endereço aqui
        // Este é apenas um exemplo simples
        return logradouro != null ? logradouro : "";
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "agente", ignore = true)
    @Mapping(target = "endereco", ignore = true)
    void updateEntity(VisitaUpdateStatusDto dto, @MappingTarget Visita entity);
}

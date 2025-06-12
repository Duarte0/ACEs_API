package org.example.aces_api.unitests.mapper;

import org.example.aces_api.dto.AreaCreateDto;
import org.example.aces_api.dto.AreaResponseDto;
import org.example.aces_api.model.entity.Area;
import org.example.aces_api.mapper.AreaMapper; // Importe sua interface de mapper
import org.example.aces_api.unitests.mapper.mocks.MockArea;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers; // Importe esta classe para obter a instância do mapper

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class AreaMapperTest {

    private AreaMapper mapper;
    private MockArea inputObject;

    @BeforeEach
    public void setup() {
        // Obter a instância do mapper gerada pelo MapStruct
        mapper = Mappers.getMapper(AreaMapper.class);
        inputObject = new MockArea();
    }

    @Test
    public void testToDtoFromEntity() {
        Area entity = inputObject.mockEntity();
        AreaResponseDto dto = mapper.toDto(entity);

        assertNotNull(dto);
        assertEquals(entity.getId(), dto.id());
        assertEquals(entity.getNome(), dto.nome());
        assertEquals(entity.getDescricao(), dto.descricao());
        assertEquals(entity.getRegiao(), dto.regiao());
        assertEquals(entity.getPopulacaoAprox(), dto.populacaoAprox());
        assertEquals(entity.getNivelRisco(), dto.nivelRisco());
        assertEquals(entity.getPrioridade(), dto.prioridade());
        assertEquals(entity.getDataUltimaAtt(), dto.dataUltimaAtt());
    }

    @Test
    public void testToEntityFromResponseDto() {
        AreaResponseDto dto = inputObject.mockResponseDto();
        Area entity = mapper.toEntity(dto); // Assumindo que você tem um toEntity para AreaResponseDto no seu mapper, ou precisa adicionar

        assertNotNull(entity);
        assertEquals(dto.id(), entity.getId());
        assertEquals(dto.nome(), entity.getNome());
        assertEquals(dto.descricao(), entity.getDescricao());
        assertEquals(dto.regiao(), entity.getRegiao());
        assertEquals(dto.populacaoAprox(), entity.getPopulacaoAprox());
        assertEquals(dto.nivelRisco(), entity.getNivelRisco());
        assertEquals(dto.prioridade(), entity.getPrioridade());
        assertEquals(dto.dataUltimaAtt(), entity.getDataUltimaAtt());
    }

    @Test
    public void testToEntityFromCreateDto() {
        AreaCreateDto createDto = inputObject.mockCreateDto();
        Area entity = mapper.toEntity(createDto);

        assertNotNull(entity);
        // IDs gerados pela estratégia GenerationType.IDENTITY não devem ser setados no DTO de criação
        // assertEquals(0, entity.getId()); // O id deve ser 0 ou null antes de ser persistido
        assertEquals(createDto.nome(), entity.getNome());
        assertEquals(createDto.descricao(), entity.getDescricao());
        assertEquals(createDto.regiao(), entity.getRegiao());
        assertEquals(createDto.populacaoAprox(), entity.getPopulacaoAprox());
        assertEquals(createDto.nivelRisco(), entity.getNivelRisco());
        assertEquals(createDto.prioridade(), entity.getPrioridade());
        // dataUltimaAtt não está presente no CreateDto, então pode ser null ou o mapper pode ter uma regra para preenchê-la
        assertNull(entity.getDataUltimaAtt()); // Assumindo que não é mapeado do CreateDto
    }

    @Test
    public void testToDtoListFromEntityList() {
        List<Area> entities = inputObject.mockEntityList();
        List<AreaResponseDto> dtos = mapper.toDto(entities);

        assertNotNull(dtos);
        assertFalse(dtos.isEmpty());
        assertEquals(entities.size(), dtos.size());

        // Testar alguns elementos específicos da lista
        Area entityZero = entities.get(0);
        AreaResponseDto dtoZero = dtos.get(0);
        assertEquals(entityZero.getId(), dtoZero.id());
        assertEquals(entityZero.getNome(), dtoZero.nome());
        assertEquals(entityZero.getNivelRisco(), dtoZero.nivelRisco());

        Area entitySeven = entities.get(7);
        AreaResponseDto dtoSeven = dtos.get(7);
        assertEquals(entitySeven.getId(), dtoSeven.id());
        assertEquals(entitySeven.getNome(), dtoSeven.nome());
        assertEquals(entitySeven.getNivelRisco(), dtoSeven.nivelRisco());
    }

    @Test
    public void testToEntityListFromResponseDtoList() {
        List<AreaResponseDto> dtos = inputObject.mockResponseDtoList();
        List<Area> entities = mapper.toEntity(dtos);

        assertNotNull(entities);
        assertFalse(entities.isEmpty());
        assertEquals(dtos.size(), entities.size());

        // Testar alguns elementos específicos da lista
        AreaResponseDto dtoZero = dtos.get(0);
        Area entityZero = entities.get(0);
        assertEquals(dtoZero.id(), entityZero.getId());
        assertEquals(dtoZero.nome(), entityZero.getNome());
        assertEquals(dtoZero.nivelRisco(), entityZero.getNivelRisco());

        AreaResponseDto dtoSeven = dtos.get(7);
        Area entitySeven = entities.get(7);
        assertEquals(dtoSeven.id(), entitySeven.getId());
        assertEquals(dtoSeven.nome(), entitySeven.getNome());
        assertEquals(dtoSeven.nivelRisco(), entitySeven.getNivelRisco());
    }

    // Exemplo de teste para verificar comportamento com entrada nula
    @Test
    public void testToDtoWithNullEntity() {
        AreaResponseDto dto = mapper.toDto((Area) null);
        assertNull(dto);
    }

    @Test
    public void testToEntityFromNullCreateDto() {
        Area entity = mapper.toEntity((AreaCreateDto) null); // Cast para especificar qual toEntity
        assertNull(entity);
    }

    @Test
    public void testToDtoListWithNullInputList() {
        List<AreaResponseDto> dtos = mapper.toDto((List<Area>) null);
        assertNull(dtos);
    }
}
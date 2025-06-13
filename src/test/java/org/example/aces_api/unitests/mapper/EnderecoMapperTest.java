package org.example.aces_api.unitests.mapper;

import org.example.aces_api.dto.EnderecoCreateDto;
import org.example.aces_api.dto.EnderecoResponseDto;
import org.example.aces_api.model.entity.Endereco;
import org.example.aces_api.mapper.EnderecoMapper; // Importe sua interface de mapper
import org.example.aces_api.unitests.mapper.mocks.EnderecoMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class EnderecoMapperTest {

    private EnderecoMapper mapper;
    private EnderecoMock inputObject;

    @BeforeEach
    public void setup() {
        mapper = Mappers.getMapper(EnderecoMapper.class);
        inputObject = new EnderecoMock();
    }

    @Test
    public void testToDtoFromEntity() {
        Endereco entity = inputObject.mockEntity();
        EnderecoResponseDto dto = mapper.toDto(entity);

        assertNotNull(dto);
        assertEquals(entity.getId(), dto.id());
        // Mapeamento especial: area.id -> area_id
        assertEquals(entity.getArea() != null ? entity.getArea().getId() : null, dto.area_id());
        assertEquals(entity.getLogradouro(), dto.logradouro());
        assertEquals(entity.getNumero(), dto.numero());
        assertEquals(entity.getBairro(), dto.bairro());
        assertEquals(entity.getCep(), dto.cep());
        assertEquals(entity.getLatitude(), dto.latitude());
        assertEquals(entity.getLongitude(), dto.longitude());
        assertEquals(entity.getSpolmovel(), dto.spolmovel());
    }

    @Test
    public void testToEntityFromCreateDto() {
        EnderecoCreateDto createDto = inputObject.mockCreateDto();
        Endereco entity = mapper.toEntity(createDto);

        assertNotNull(entity);
        // ID é gerado pela estratégia GenerationType.SEQUENCE, então esperamos 0 antes de persistir
        // assertEquals(0, entity.getId());
        // area_id deve ser usado para buscar ou criar a Area no serviço, não no mapper
        assertNull(entity.getArea()); // Esperamos que a Area seja null, pois o DTO só tem o ID
        assertEquals(createDto.logradouro(), entity.getLogradouro());
        assertEquals(createDto.numero(), entity.getNumero());
        assertEquals(createDto.bairro(), entity.getBairro());
        assertEquals(createDto.cep(), entity.getCep());
        assertEquals(createDto.latitude(), entity.getLatitude());
        assertEquals(createDto.longitude(), entity.getLongitude());
        assertEquals(createDto.spolmovel(), entity.getSpolmovel());
    }

    @Test
    public void testToDtoListFromEntityList() {
        List<Endereco> entities = inputObject.mockEntityList();
        List<EnderecoResponseDto> dtos = mapper.toDto(entities);

        assertNotNull(dtos);
        assertFalse(dtos.isEmpty());
        assertEquals(entities.size(), dtos.size());

        // Testar alguns elementos específicos da lista
        Endereco entityZero = entities.get(0);
        EnderecoResponseDto dtoZero = dtos.get(0);
        assertEquals(entityZero.getId(), dtoZero.id());
        assertEquals(entityZero.getArea().getId(), dtoZero.area_id());
        assertEquals(entityZero.getLogradouro(), dtoZero.logradouro());
        assertEquals(entityZero.getSpolmovel(), dtoZero.spolmovel());

        Endereco entitySeven = entities.get(7);
        EnderecoResponseDto dtoSeven = dtos.get(7);
        assertEquals(entitySeven.getId(), dtoSeven.id());
        assertEquals(entitySeven.getArea().getId(), dtoSeven.area_id());
        assertEquals(entitySeven.getLogradouro(), dtoSeven.logradouro());
        assertEquals(entitySeven.getSpolmovel(), dtoSeven.spolmovel());
    }

     @Test
     public void testToEntityFromResponseDto() {
         EnderecoResponseDto dto = inputObject.mockResponseDto();
         Endereco entity = mapper.toEntity(dto);

         assertNotNull(entity);
         assertEquals(dto.id(), entity.getId());
         assertNull(entity.getArea());
         assertEquals(dto.logradouro(), entity.getLogradouro());
     }

    @Test
    public void testToDtoListWithNullInputList() {
        List<EnderecoResponseDto> dtos = mapper.toDto((List<Endereco>) null);
        assertNull(dtos);
    }

    @Test
    public void testToEntityFromNullCreateDto() {
        Endereco entity = mapper.toEntity((EnderecoCreateDto) null);
        assertNull(entity);
    }
}
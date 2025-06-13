package org.example.aces_api.unitests.mapper;

import org.example.aces_api.dto.PacienteCreateDto;
import org.example.aces_api.dto.PacienteResponseDto;
import org.example.aces_api.model.entity.Paciente;
import org.example.aces_api.mapper.PacienteMapper;
import org.example.aces_api.unitests.mapper.mocks.PacienteMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PacienteMapperTest {

    private PacienteMapper mapper;
    private PacienteMock inputObject;

    @BeforeEach
    public void setup() {
        mapper = Mappers.getMapper(PacienteMapper.class);
        inputObject = new PacienteMock();
    }

    @Test
    public void testToDtoFromEntity() {
        Paciente entity = inputObject.mockEntity();
        PacienteResponseDto dto = mapper.toDto(entity);

        assertNotNull(dto);
        assertEquals(entity.getId(), dto.id());
        assertEquals(entity.getEndereco() != null ? entity.getEndereco().getId() : null, dto.endereco_id());
        assertEquals(entity.getNome(), dto.nome());
        assertEquals(entity.getDataNascimento(), dto.dataNascimento());
        assertEquals(entity.getSexo(), dto.sexo());
        assertEquals(entity.getCpf(), dto.cpf());
        assertEquals(entity.getCartaoSUS(), dto.cartaoSUS());
        assertEquals(entity.getTelefone(), dto.telefone());
    }

    @Test
    public void testToEntityFromResponseDto() {
        PacienteResponseDto dto = inputObject.mockResponseDto();
        Paciente entity = mapper.toEntity(dto);

        assertNotNull(entity);
        assertEquals(dto.id(), entity.getId());
        assertNull(entity.getEndereco());
        assertEquals(dto.nome(), entity.getNome());
        assertEquals(dto.dataNascimento(), entity.getDataNascimento());
        assertEquals(dto.sexo(), entity.getSexo());
        assertEquals(dto.cpf(), entity.getCpf());
        assertEquals(dto.cartaoSUS(), entity.getCartaoSUS());
        assertEquals(dto.telefone(), entity.getTelefone());
    }

    @Test
    public void testToEntityFromCreateDto() {
        PacienteCreateDto createDto = inputObject.mockCreateDto();
        Paciente entity = mapper.toEntity(createDto);

        assertNotNull(entity);
        assertNull(entity.getEndereco());
        assertEquals(createDto.nome(), entity.getNome());
        assertEquals(createDto.dataNascimento(), entity.getDataNascimento());
        assertEquals(createDto.sexo(), entity.getSexo());
        assertEquals(createDto.cpf(), entity.getCpf());
        assertEquals(createDto.cartaoSUS(), entity.getCartaoSUS());
        assertEquals(createDto.telefone(), entity.getTelefone());
    }

    @Test
    public void testToDtoListFromEntityList() {
        List<Paciente> entities = inputObject.mockEntityList();
        List<PacienteResponseDto> dtos = mapper.toDto(entities);

        assertNotNull(dtos);
        assertFalse(dtos.isEmpty());
        assertEquals(entities.size(), dtos.size());


        Paciente entityZero = entities.get(0);
        PacienteResponseDto dtoZero = dtos.get(0);
        assertEquals(entityZero.getId(), dtoZero.id());
        assertEquals(entityZero.getEndereco().getId(), dtoZero.endereco_id());
        assertEquals(entityZero.getNome(), dtoZero.nome());
        assertEquals(entityZero.getSexo(), dtoZero.sexo());

        Paciente entitySeven = entities.get(7);
        PacienteResponseDto dtoSeven = dtos.get(7);
        assertEquals(entitySeven.getId(), dtoSeven.id());
        assertEquals(entitySeven.getEndereco().getId(), dtoSeven.endereco_id());
        assertEquals(entitySeven.getNome(), dtoSeven.nome());
        assertEquals(entitySeven.getSexo(), dtoSeven.sexo());
    }

    @Test
    public void testToEntityListFromResponseDtoList() {
        List<PacienteResponseDto> dtos = inputObject.mockResponseDtoList();
        List<Paciente> entities = mapper.toEntity(dtos);

        assertNotNull(entities);
        assertFalse(entities.isEmpty());
        assertEquals(dtos.size(), entities.size());


        PacienteResponseDto dtoZero = dtos.get(0);
        Paciente entityZero = entities.get(0);
        assertEquals(dtoZero.id(), entityZero.getId());
        assertNull(entityZero.getEndereco());
        assertEquals(dtoZero.nome(), entityZero.getNome());
        assertEquals(dtoZero.sexo(), entityZero.getSexo());

        PacienteResponseDto dtoSeven = dtos.get(7);
        Paciente entitySeven = entities.get(7);
        assertEquals(dtoSeven.id(), entitySeven.getId());
        assertNull(entitySeven.getEndereco());
        assertEquals(dtoSeven.nome(), entitySeven.getNome());
        assertEquals(dtoSeven.sexo(), entitySeven.getSexo());
    }


    @Test
    public void testToDtoWithNullEntity() {
        PacienteResponseDto dto = mapper.toDto((Paciente) null);
        assertNull(dto);
    }

    @Test
    public void testToEntityFromNullCreateDto() {
        Paciente entity = mapper.toEntity((PacienteCreateDto) null);
        assertNull(entity);
    }

    @Test
    public void testToDtoListWithNullInputList() {
        List<PacienteResponseDto> dtos = mapper.toDto((List<Paciente>) null);
        assertNull(dtos);
    }

    @Test
    public void testToEntityListFromNullResponseDtoList() {
        List<Paciente> entities = mapper.toEntity((List<PacienteResponseDto>) null);
        assertNull(entities);
    }
}
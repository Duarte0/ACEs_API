package org.example.aces_api.unitests.mapper.mocks;

import org.example.aces_api.dto.AreaCreateDto;
import org.example.aces_api.dto.AreaResponseDto;
import org.example.aces_api.dto.Risco;
import org.example.aces_api.model.entity.Area;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AreaMock {

    public Area mockEntity() {
        return mockEntity(0);
    }

    public AreaCreateDto mockCreateDto() {
        return mockCreateDto(0);
    }

    public AreaResponseDto mockResponseDto() {
        return mockResponseDto(0);
    }

    public Area mockEntity(int number) {
        Area area = new Area();
        area.setId(number);
        area.setNome("Nome da Área Teste " + number);
        area.setDescricao("Descrição da Área Teste " + number);
        area.setRegiao("Região " + number);
        area.setPopulacaoAprox(1000 + number * 100);
        area.setNivelRisco((number % 4 == 0) ? Risco.BAIXO : (number % 4 == 1) ? Risco.MEDIO : (number % 4 == 2) ? Risco.ALTO : Risco.MUITO_ALTO);
        area.setPrioridade((number % 4 == 0) ? Risco.ALTO : (number % 4 == 1) ? Risco.BAIXO : (number % 4 == 2) ? Risco.MEDIO : Risco.MUITO_ALTO);
        area.setDataUltimaAtt(LocalDateTime.of(2023, 1, 1, 10, 0).plusDays(number));
        return area;
    }

    public AreaCreateDto mockCreateDto(int number) {
        return new AreaCreateDto(
                "Nome da Área Teste " + number,
                "Descrição da Área Teste " + number,
                "Região " + number,
                1000 + number * 100,
                (number % 4 == 0) ? Risco.BAIXO : (number % 4 == 1) ? Risco.MEDIO : (number % 4 == 2) ? Risco.ALTO : Risco.MUITO_ALTO,
                (number % 4 == 0) ? Risco.ALTO : (number % 4 == 1) ? Risco.BAIXO : (number % 4 == 2) ? Risco.MEDIO : Risco.MUITO_ALTO
        );
    }

    public AreaResponseDto mockResponseDto(int number) {
        return new AreaResponseDto(
                number,
                "Nome da Área Teste " + number,
                "Descrição da Área Teste " + number,
                "Região " + number,
                1000 + number * 100,
                (number % 4 == 0) ? Risco.BAIXO : (number % 4 == 1) ? Risco.MEDIO : (number % 4 == 2) ? Risco.ALTO : Risco.MUITO_ALTO,
                (number % 4 == 0) ? Risco.ALTO : (number % 4 == 1) ? Risco.BAIXO : (number % 4 == 2) ? Risco.MEDIO : Risco.MUITO_ALTO,
                LocalDateTime.of(2023, 1, 1, 10, 0).plusDays(number)
        );
    }

    public List<Area> mockEntityList() {
        List<Area> areas = new ArrayList<>();
        for (int i = 0; i < 14; i++) {
            areas.add(mockEntity(i));
        }
        return areas;
    }

    public List<AreaCreateDto> mockCreateDtoList() {
        List<AreaCreateDto> dtos = new ArrayList<>();
        for (int i = 0; i < 14; i++) {
            dtos.add(mockCreateDto(i));
        }
        return dtos;
    }

    public List<AreaResponseDto> mockResponseDtoList() {
        List<AreaResponseDto> dtos = new ArrayList<>();
        for (int i = 0; i < 14; i++) {
            dtos.add(mockResponseDto(i));
        }
        return dtos;
    }
}
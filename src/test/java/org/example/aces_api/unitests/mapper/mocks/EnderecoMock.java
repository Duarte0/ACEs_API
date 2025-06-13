package org.example.aces_api.unitests.mapper.mocks;

import org.example.aces_api.dto.EnderecoCreateDto;
import org.example.aces_api.dto.EnderecoResponseDto;
import org.example.aces_api.model.entity.Area; // Importe a classe Area
import org.example.aces_api.model.entity.Endereco;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class EnderecoMock {

    // Método auxiliar para criar uma Area mock
    private Area mockArea(int id) {
        Area area = new Area();
        area.setId(id);
        area.setNome("Área Mock " + id);
        // Preencher outros campos da Area se necessário para o mapeamento ou lógica
        return area;
    }

    public Endereco mockEntity() {
        return mockEntity(0);
    }

    public EnderecoCreateDto mockCreateDto() {
        return mockCreateDto(0);
    }

    public EnderecoResponseDto mockResponseDto() {
        return mockResponseDto(0);
    }

    public Endereco mockEntity(int number) {
        Endereco endereco = new Endereco();
        endereco.setId(number);
        endereco.setArea(mockArea(number + 100)); // Associa uma Area mockada
        endereco.setLogradouro("Rua das Flores " + number);
        endereco.setNumero(String.valueOf(number + 10));
        endereco.setBairro("Centro " + number);
        endereco.setCep("00000-00" + (number % 10));
        endereco.setLatitude(BigDecimal.valueOf(-15.000 + number * 0.01));
        endereco.setLongitude(BigDecimal.valueOf(-47.000 + number * 0.01));
        endereco.setSpolmovel(number % 2 == 0); // Alterna true/false
        return endereco;
    }

    public EnderecoCreateDto mockCreateDto(int number) {
        return new EnderecoCreateDto(
                number + 100, // area_id
                "Rua das Flores " + number,
                String.valueOf(number + 10),
                "Centro " + number,
                "00000-00" + (number % 10),
                BigDecimal.valueOf(-15.000 + number * 0.01),
                BigDecimal.valueOf(-47.000 + number * 0.01),
                number % 2 == 0
        );
    }

    public EnderecoResponseDto mockResponseDto(int number) {
        return new EnderecoResponseDto(
                number,
                number + 100, // area_id
                "Rua das Flores " + number,
                String.valueOf(number + 10),
                "Centro " + number,
                "00000-00" + (number % 10),
                BigDecimal.valueOf(-15.000 + number * 0.01),
                BigDecimal.valueOf(-47.000 + number * 0.01),
                number % 2 == 0
        );
    }

    public List<Endereco> mockEntityList() {
        List<Endereco> enderecos = new ArrayList<>();
        for (int i = 0; i < 14; i++) {
            enderecos.add(mockEntity(i));
        }
        return enderecos;
    }

    public List<EnderecoCreateDto> mockCreateDtoList() {
        List<EnderecoCreateDto> dtos = new ArrayList<>();
        for (int i = 0; i < 14; i++) {
            dtos.add(mockCreateDto(i));
        }
        return dtos;
    }

    public List<EnderecoResponseDto> mockResponseDtoList() {
        List<EnderecoResponseDto> dtos = new ArrayList<>();
        for (int i = 0; i < 14; i++) {
            dtos.add(mockResponseDto(i));
        }
        return dtos;
    }
}
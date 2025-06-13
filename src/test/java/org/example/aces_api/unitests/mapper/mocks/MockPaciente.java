package org.example.aces_api.unitests.mapper.mocks;

import org.example.aces_api.dto.PacienteCreateDto;
import org.example.aces_api.dto.PacienteResponseDto;
import org.example.aces_api.dto.Sexo;
import org.example.aces_api.model.entity.Endereco;
import org.example.aces_api.model.entity.Paciente;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MockPaciente {

    // Método auxiliar para criar um Endereco mock
    private Endereco mockEndereco(int id) {
        Endereco endereco = new Endereco();
        endereco.setId(id);
        endereco.setLogradouro("Rua Teste " + id);
        endereco.setNumero("N " + id);
        endereco.setBairro("Bairro Teste " + id);
        endereco.setCep("12345-67" + (id % 10));
        endereco.setLatitude(BigDecimal.valueOf(-16.0 + id * 0.01));
        endereco.setLongitude(BigDecimal.valueOf(-48.0 + id * 0.01));
        endereco.setSpolmovel(id % 2 == 0);
        return endereco;
    }

    public Paciente mockEntity() {
        return mockEntity(0);
    }

    public PacienteCreateDto mockCreateDto() {
        return mockCreateDto(0);
    }

    public PacienteResponseDto mockResponseDto() {
        return mockResponseDto(0);
    }

    public Paciente mockEntity(int number) {
        Paciente paciente = new Paciente();
        paciente.setId(number);
        paciente.setEndereco(mockEndereco(number + 100)); // Adiciona um Endereco mock com ID
        paciente.setNome("Paciente Teste " + number);
        paciente.setDataNascimento(LocalDate.of(1990, 1, 1).plusDays(number));
        paciente.setSexo((number % 2 == 0) ? Sexo.MASCULINO : Sexo.FEMININO);
        paciente.setCpf(String.format("%011d", number));
        paciente.setCartaoSUS("SUS" + String.format("%017d", number));
        paciente.setTelefone("119" + String.format("%08d", number));
        return paciente;
    }

    public PacienteCreateDto mockCreateDto(int number) {
        return new PacienteCreateDto(
                number + 100, // ID do endereço
                "Paciente Teste " + number,
                LocalDate.of(1990, 1, 1).plusDays(number),
                (number % 2 == 0) ? Sexo.MASCULINO : Sexo.FEMININO,
                String.format("%011d", number),
                "SUS" + String.format("%017d", number),
                "119" + String.format("%08d", number)
        );
    }

    public PacienteResponseDto mockResponseDto(int number) {
        return new PacienteResponseDto(
                number,
                number + 100, // ID do endereço
                "Paciente Teste " + number,
                LocalDate.of(1990, 1, 1).plusDays(number),
                (number % 2 == 0) ? Sexo.MASCULINO : Sexo.FEMININO,
                String.format("%011d", number),
                "SUS" + String.format("%017d", number),
                "119" + String.format("%08d", number)
        );
    }

    public List<Paciente> mockEntityList() {
        List<Paciente> pacientes = new ArrayList<>();
        for (int i = 0; i < 14; i++) {
            pacientes.add(mockEntity(i));
        }
        return pacientes;
    }

    public List<PacienteCreateDto> mockCreateDtoList() {
        List<PacienteCreateDto> dtos = new ArrayList<>();
        for (int i = 0; i < 14; i++) {
            dtos.add(mockCreateDto(i));
        }
        return dtos;
    }

    public List<PacienteResponseDto> mockResponseDtoList() {
        List<PacienteResponseDto> dtos = new ArrayList<>();
        for (int i = 0; i < 14; i++) {
            dtos.add(mockResponseDto(i));
        }
        return dtos;
    }
}
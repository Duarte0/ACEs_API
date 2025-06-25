package org.example.aces_api.unitests.mapper.mocks;

import org.example.aces_api.dto.VisitaCreateDto;
import org.example.aces_api.dto.VisitaResponseDto;
import org.example.aces_api.model.entity.Agente;
import org.example.aces_api.model.entity.Endereco;
import org.example.aces_api.model.entity.Visita;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class VisitaMock {

    // Métodos auxiliares para criar objetos mock relacionados
    private Agente mockAgente(int id) {
        Agente agente = new Agente();
        agente.setId(id);
        agente.setNome("Agente Teste " + id);
        // Adicione outros campos necessários para o Agente
        return agente;
    }

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

    public Visita mockEntity() {
        return mockEntity(0);
    }

    public VisitaCreateDto mockCreateDto() {
        return mockCreateDto(0);
    }

    public VisitaResponseDto mockResponseDto() {
        return mockResponseDto(0);
    }

    public Visita mockEntity(int number) {
        Visita visita = new Visita();
        visita.setId(number);
        visita.setAgente(mockAgente(number + 200)); // Adiciona um Agente mock com ID
        visita.setEndereco(mockEndereco(number + 100)); // Adiciona um Endereco mock com ID
        visita.setDataHora(LocalDateTime.of(2025, 6, 24, 10, 0).plusHours(number));
        visita.setObservacoes("Observação da visita " + number);
        visita.setStatus("Status " + (number % 3 == 0 ? "Agendada" : number % 3 == 1 ? "Em andamento" : "Concluída"));
        visita.setFoiRealizada(number % 2 == 0);
        return visita;
    }

    public VisitaCreateDto mockCreateDto(int number) {
        return new VisitaCreateDto(
                (int) (number + 200), // ID do agente
                (int) (number + 100), // ID do endereço
                LocalDateTime.of(2025, 6, 24, 10, 0).plusHours(number),
                "Observação da visita " + number,
                "Status " + (number % 3 == 0 ? "Agendada" : number % 3 == 1 ? "Em andamento" : "Concluída"),
                number % 2 == 0
        );
    }

    public VisitaResponseDto mockResponseDto(int number) {
        return new VisitaResponseDto(
                number,
                (int) (number + 200), // ID do agente
                (int) (number + 100), // ID do endereço
                LocalDateTime.of(2025, 6, 24, 10, 0).plusHours(number),
                "Observação da visita " + number,
                "Status " + (number % 3 == 0 ? "Agendada" : number % 3 == 1 ? "Em andamento" : "Concluída"),
                number % 2 == 0
        );
    }

    public List<Visita> mockEntityList() {
        List<Visita> visitas = new ArrayList<>();
        for (int i = 0; i < 14; i++) {
            visitas.add(mockEntity(i));
        }
        return visitas;
    }

    public List<VisitaCreateDto> mockCreateDtoList() {
        List<VisitaCreateDto> dtos = new ArrayList<>();
        for (int i = 0; i < 14; i++) {
            dtos.add(mockCreateDto(i));
        }
        return dtos;
    }

    public List<VisitaResponseDto> mockResponseDtoList() {
        List<VisitaResponseDto> dtos = new ArrayList<>();
        for (int i = 0; i < 14; i++) {
            dtos.add(mockResponseDto(i));
        }
        return dtos;
    }
}

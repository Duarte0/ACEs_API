package org.example.aces_api.service;

import org.example.aces_api.dto.AreaCreateDto;
import org.example.aces_api.dto.FullReportDto;
import org.example.aces_api.dto.AreaResponseDto;
import org.example.aces_api.dto.RegionalReportDto;
import org.example.aces_api.exception.EntityNotFoundException;
import org.example.aces_api.mapper.AreaMapper;
import org.example.aces_api.model.entity.Area;
import org.example.aces_api.model.repository.AreaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AreaService {

    @Autowired
    private AreaRepository repository;

    @Autowired
    private AreaMapper mapper;

    public AreaResponseDto findById(Integer id) {
        var area = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Area com ID " + id + " não encontrada."));
        return mapper.toDto(area);
    }

    public AreaResponseDto criarArea(AreaCreateDto areaCreate){

        var entity = mapper.toEntity(areaCreate);
        entity.setDataUltimaAtt(LocalDateTime.now());
        var area = repository.save(entity);

        return mapper.toDto(area);
    }

    public AreaResponseDto atualizarArea(Integer id, AreaCreateDto areaCreate){

        var entity = repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Area com ID " + id + " não encontrada."));

        entity.setNome(areaCreate.nome());
        entity.setDescricao(areaCreate.descricao());
        entity.setRegiao(areaCreate.regiao());
        entity.setPopulacaoAprox(areaCreate.populacaoAprox());
        entity.setNivelRisco(areaCreate.nivelRisco());
        entity.setPrioridade(areaCreate.prioridade());
        entity.setDataUltimaAtt(LocalDateTime.now());

        var area = repository.save(entity);
        return mapper.toDto(area);

    }

    public List<AreaResponseDto> findAll(){
        return mapper.toDto(repository.findAll());

    }

    public void excluirArea(Integer id){

        var area = repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Area com ID " + id + " não encontrada."));

        repository.delete(area);
    }

    public FullReportDto gerarRelatorio(){
        List<Area> allAreas = repository.findAll();

        if (allAreas.isEmpty()) {
            return new FullReportDto(List.of(), 0.0, 0.0, 0L); // Retorna um relatório vazio
        }

        // --- Agrupar por Região e Calcular Métricas Regionais ---
        Map<String, List<Area>> areasPorRegiao = allAreas.stream()
                .collect(Collectors.groupingBy(Area::getRegiao));

        List<RegionalReportDto> relatorioPorRegiao = areasPorRegiao.entrySet().stream()
                .map(entry -> {
                    String regiao = entry.getKey();
                    List<Area> areasDaRegiao = entry.getValue();

                    List<String> niveisRisco = areasDaRegiao.stream()
                            .map(Area::getNivelRisco)
                            .collect(Collectors.toList());

                    List<String> prioridades = areasDaRegiao.stream()
                            .map(Area::getPrioridade)
                            .collect(Collectors.toList());

                    double mediaNivelRiscoRegional = areasDaRegiao.stream()
                            .mapToInt(area -> {
                                try {
                                    return Integer.parseInt(area.getNivelRisco());
                                } catch (NumberFormatException e) {
                                    System.err.println("Nível de Risco inválido em região '" + regiao + "': " + area.getNivelRisco());
                                    return 0;
                                }
                            })
                            .average()
                            .orElse(0.0);

                    double mediaPrioridadeRegional = areasDaRegiao.stream()
                            .mapToInt(area -> {
                                try {
                                    return Integer.parseInt(area.getPrioridade());
                                } catch (NumberFormatException e) {
                                    System.err.println("Prioridade inválida em região '" + regiao + "': " + area.getPrioridade());
                                    return 0;
                                }
                            })
                            .average()
                            .orElse(0.0);

                    long totalAreasNaRegiao = areasDaRegiao.size();

                    return new RegionalReportDto(regiao, niveisRisco, prioridades,
                            mediaNivelRiscoRegional, mediaPrioridadeRegional, totalAreasNaRegiao);
                })
                // ordenar as regiões no relatório final, pela média de risco
                .sorted(Comparator.comparing(RegionalReportDto::mediaNivelRiscoRegional, Comparator.reverseOrder()))
                .collect(Collectors.toList());

        // --- Calcular Métricas Globais (para o resumo geral) ---
        double mediaNivelRiscoGlobal = allAreas.stream()
                .mapToInt(area -> {
                    try {
                        return Integer.parseInt(area.getNivelRisco());
                    } catch (NumberFormatException e) {
                        System.err.println("Nível de Risco inválido para cálculo de média global: " + area.getNivelRisco());
                        return 0;
                    }
                })
                .average()
                .orElse(0.0);

        double mediaPrioridadeGlobal = allAreas.stream()
                .mapToInt(area -> {
                    try {
                        return Integer.parseInt(area.getPrioridade());
                    } catch (NumberFormatException e) {
                        System.err.println("Prioridade inválida para cálculo de média global: " + area.getPrioridade());
                        return 0;
                    }
                })
                .average()
                .orElse(0.0);

        long totalAreasGlobais = allAreas.size();

        // --- Retornar o DTO Completo do Relatório ---
        return new FullReportDto(relatorioPorRegiao, mediaNivelRiscoGlobal, mediaPrioridadeGlobal, totalAreasGlobais);
    }
}

package org.example.aces_api.service;

import org.example.aces_api.dto.AreaCreateDto;
import org.example.aces_api.dto.AreaResponseDto;
import org.example.aces_api.dto.RelatorioDTO;
import org.example.aces_api.exception.EntityNotFoundException;
import org.example.aces_api.mapper.AreaMapper;
import org.example.aces_api.model.entity.Area;
import org.example.aces_api.model.repository.AreaRepository;
import org.example.aces_api.model.repository.CasoAedesRepository;
import org.example.aces_api.model.repository.FocoAedesRepository;
import org.example.aces_api.model.repository.VisitaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class AreaService {

    @Autowired
    private AreaRepository areaRepository;
    @Autowired
    private VisitaRepository visitaRepository;
    @Autowired
    private CasoAedesRepository casoAedesRepository;
    @Autowired
    private FocoAedesRepository focoAedesRepository;

    @Autowired
    private AreaMapper mapper;

    public AreaService(AreaRepository areaRepository, VisitaRepository visitaRepository,
                       CasoAedesRepository casoAedesRepository, FocoAedesRepository focoAedesRepository) {
        this.areaRepository = areaRepository;
        this.visitaRepository = visitaRepository;
        this.casoAedesRepository = casoAedesRepository;
        this.focoAedesRepository = focoAedesRepository;
    }

    public AreaResponseDto findById(Integer id) {
        var area = areaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Area com ID " + id + " não encontrada."));
        return mapper.toDto(area);
    }

    public AreaResponseDto criarArea(AreaCreateDto areaCreate) {

        var entity = mapper.toEntity(areaCreate);
        entity.setDataUltimaAtt(LocalDateTime.now());
        var area = areaRepository.save(entity);

        return mapper.toDto(area);
    }

    public AreaResponseDto atualizarArea(Integer id, AreaCreateDto areaCreate) {

        var entity = areaRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Area com ID " + id + " não encontrada."));

        entity.setNome(areaCreate.nome());
        entity.setDescricao(areaCreate.descricao());
        entity.setRegiao(areaCreate.regiao());
        entity.setPopulacaoAprox(areaCreate.populacaoAprox());
        entity.setNivelRisco(areaCreate.nivelRisco());
        entity.setPrioridade(areaCreate.prioridade());
        entity.setDataUltimaAtt(LocalDateTime.now());

        var area = areaRepository.save(entity);
        return mapper.toDto(area);

    }

    public List<AreaResponseDto> findAll() {
        return mapper.toDto(areaRepository.findAll());

    }

    public void excluirArea(Integer id) {

        var area = areaRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Area com ID " + id + " não encontrada."));

        areaRepository.delete(area);
    }

    public RelatorioDTO gerarRelatorio(Long areaId, LocalDate dataInicio, LocalDate dataFim) {

        Area area = areaRepository.findById(Math.toIntExact(areaId))
                .orElseThrow(() -> new RuntimeException("Área com ID " + areaId + " não encontrada."));

        LocalDateTime inicioPeriodo = dataInicio.atStartOfDay();
        LocalDateTime fimPeriodo = dataFim.atTime(23, 59, 59);

        int totalVisitas = visitaRepository.countVisitasByAreaAndPeriod(areaId, inicioPeriodo, fimPeriodo);
        int totalCasos = casoAedesRepository.countDengueByAreaAndPeriod(areaId, inicioPeriodo, fimPeriodo);
        int totalFocos = focoAedesRepository.countFocosByAreaAndPeriod(areaId, inicioPeriodo, fimPeriodo);

        double indiceDengue = (totalVisitas > 0) ? ((double) totalCasos / totalVisitas) * 1000 : 0.0;

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String periodoFormatado = dataInicio.format(formatter) + " a " + dataFim.format(formatter);

        return new RelatorioDTO(
                area.getId(),
                area.getNome(),
                periodoFormatado,
                totalCasos,
                totalVisitas,
                totalFocos,
                indiceDengue,
                LocalDateTime.now()
        );
    }
}
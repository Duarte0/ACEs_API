package org.example.aces_api.service;

import org.example.aces_api.controller.AreaController; // Importar o controlador para referenciar os métodos
import org.example.aces_api.dto.AreaCreateDto;
import org.example.aces_api.dto.AreaResponseDto;
import org.example.aces_api.dto.RelatorioDTO;
import org.example.aces_api.exception.EntityNotFoundException;
import org.example.aces_api.mapper.AreaMapper;
import org.example.aces_api.model.entity.Area;
import org.example.aces_api.model.repository.AreaRepository;
import org.example.aces_api.model.repository.CasoDengueRepository;
import org.example.aces_api.model.repository.FocoAedesRepository;
import org.example.aces_api.model.repository.VisitaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.hateoas.EntityModel; 
import org.springframework.hateoas.CollectionModel;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;



@Service
public class AreaService {

    @Autowired
    private AreaRepository areaRepository;
    @Autowired
    private VisitaRepository visitaRepository;
    @Autowired
    private CasoDengueRepository casoAedesRepository;
    @Autowired
    private FocoAedesRepository focoAedesRepository;

    @Autowired
    private AreaMapper mapper;

    public AreaService(AreaRepository areaRepository, VisitaRepository visitaRepository,
                       CasoDengueRepository casoAedesRepository, FocoAedesRepository focoAedesRepository) {
        this.areaRepository = areaRepository;
        this.visitaRepository = visitaRepository;
        this.casoAedesRepository = casoAedesRepository;
        this.focoAedesRepository = focoAedesRepository;
    }

    @Cacheable(value = "areas", key = "#id", unless = "#result == null")
    public EntityModel<AreaResponseDto> findById(Integer id) {
        var area = areaRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Area com ID " + id + " não encontrada."));
        AreaResponseDto dto = mapper.toDto(area);
        // Cria um EntityModel e adiciona o link de auto-referência
        return EntityModel.of(dto,
                linkTo(methodOn(AreaController.class).getAreaById(id)) // <-- ALTERADO PARA getAreaById
                        .withSelfRel());
    }

    @CacheEvict(value = "areas", allEntries = true)
    public EntityModel<AreaResponseDto> criarArea(AreaCreateDto areaCreate){
        var entity = mapper.toEntity(areaCreate);
        entity.setDataUltimaAtt(LocalDateTime.now());
        var area = areaRepository.save(entity);

        AreaResponseDto dto = mapper.toDto(area);
        // Cria um EntityModel e adiciona o link para o recurso recém-criado
        return EntityModel.of(dto,
                linkTo(methodOn(AreaController.class).getAreaById(dto.id())).withSelfRel());
    }

    @CachePut(value = "areas", key = "#id")
    @CacheEvict(value = "areas", key = "'allAreas'")
    public EntityModel<AreaResponseDto> atualizarArea(Integer id, AreaCreateDto areaCreate){
        var entity = areaRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Area com ID " + id + " não encontrada."));
        entity.setNome(areaCreate.nome());
        entity.setDescricao(areaCreate.descricao());
        entity.setRegiao(areaCreate.regiao());
        entity.setPopulacaoAprox(areaCreate.populacaoAprox());
        entity.setNivelRisco(areaCreate.nivelRisco());
        entity.setPrioridade(areaCreate.prioridade());
        entity.setDataUltimaAtt(LocalDateTime.now());
      
        var area = areaRepository.save(entity);
        AreaResponseDto dto = mapper.toDto(area);
        // Retorna o EntityModel sem adicionar link Self, como solicitado anteriormente
        return EntityModel.of(dto);
    }

    @Cacheable(value = "areas", key = "'allAreas'")
    public CollectionModel<EntityModel<AreaResponseDto>> findAll(){

        List<EntityModel<AreaResponseDto>> areasComLinks = areaRepository.findAll().stream()
                .map(area -> {
                    AreaResponseDto dto = mapper.toDto(area);
                 return EntityModel.of(dto,
                            linkTo(methodOn(AreaController.class).getAreaById(dto.id())).withSelfRel());
                })
                .collect(Collectors.toList());

        return CollectionModel.of(areasComLinks,
                linkTo(methodOn(AreaController.class).getAllAreas()).withSelfRel());
    }

    @CacheEvict(value = "areas", key = "#id")
    public void excluirArea(Integer id){
        var area = areaRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Area com ID " + id + " não encontrada."));
        areaRepository.delete(area);
      }

    public EntityModel<RelatorioDTO> gerarRelatorio(Long areaId, LocalDate dataInicio, LocalDate dataFim) {

        Area area = areaRepository.findById(Math.toIntExact(areaId))
                .orElseThrow(() -> new EntityNotFoundException("Área com ID " + areaId + " não encontrada."));

        LocalDateTime inicioPeriodo = dataInicio.atStartOfDay();
        LocalDateTime fimPeriodo = dataFim.atTime(23, 59, 59);

        int totalVisitas = visitaRepository.countVisitasByAreaAndPeriod(areaId, inicioPeriodo, fimPeriodo);
        int totalCasos = casoAedesRepository.countDengueByAreaAndPeriod(areaId, inicioPeriodo, fimPeriodo);
        int totalFocos = focoAedesRepository.countFocosByAreaAndPeriod(areaId, inicioPeriodo, fimPeriodo);

        double indiceDengue = (totalVisitas > 0) ? ((double) totalCasos / totalVisitas) * 1000 : 0.0;

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String periodoFormatado = dataInicio.format(formatter) + " a " + dataFim.format(formatter);

        RelatorioDTO relatorioDTO = new RelatorioDTO(
                area.getId(),
                area.getNome(),
                periodoFormatado,
                totalCasos,
                totalVisitas,
                totalFocos,
                indiceDengue,
                LocalDateTime.now()
        );

        return EntityModel.of(relatorioDTO,
                linkTo(methodOn(AreaController.class)
                        .getRelatorioDaArea(areaId, dataInicio.toString(), dataFim.toString()))
                        .withSelfRel(),
                linkTo(methodOn(AreaController.class)
                        .getAreaById(Math.toIntExact(areaId)))
                        .withRel("area")
        );
    }
}
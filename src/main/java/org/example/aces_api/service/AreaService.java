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

      // Tipo de retorno alterado para EntityModel<AreaResponseDto>
    public EntityModel<AreaResponseDto> findById(Integer id) {
        var area = areaRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Area com ID " + id + " não encontrada."));
        AreaResponseDto dto = mapper.toDto(area);
        // Cria um EntityModel e adiciona o link de auto-referência
        return EntityModel.of(dto,
                linkTo(methodOn(AreaController.class).getAreaById(id)) // <-- ALTERADO PARA getAreaById
                        .withSelfRel());
    }

    public EntityModel<AreaResponseDto> criarArea(AreaCreateDto areaCreate){
        var entity = mapper.toEntity(areaCreate);
        entity.setDataUltimaAtt(LocalDateTime.now());
        var area = areaRepository.save(entity);

        AreaResponseDto dto = mapper.toDto(area);
        // Cria um EntityModel e adiciona o link para o recurso recém-criado
        return EntityModel.of(dto,
                linkTo(methodOn(AreaController.class).getAreaById(dto.id())).withSelfRel());
    }

  
    // Tipo de retorno alterado para EntityModel<AreaResponseDto>
    // Conforme solicitado anteriormente, este método não adiciona um link de auto-referência ao próprio recurso atualizado.
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

    // Tipo de retorno alterado para CollectionModel<EntityModel<AreaResponseDto>>
    public CollectionModel<EntityModel<AreaResponseDto>> findAll(){
        // Mapeia cada Area para AreaResponseDto, então encapsula em EntityModel com link Self
        List<EntityModel<AreaResponseDto>> areasComLinks = areaRepository.findAll().stream()
                .map(area -> {
                    AreaResponseDto dto = mapper.toDto(area);
                 return EntityModel.of(dto,
                            linkTo(methodOn(AreaController.class).getAreaById(dto.id())).withSelfRel());
                })
                .collect(Collectors.toList());

        // Retorna uma CollectionModel da lista de EntityModels, com um link Self para a própria coleção
        return CollectionModel.of(areasComLinks,
                linkTo(methodOn(AreaController.class).getAllAreas()).withSelfRel());
    }
  
    public void excluirArea(Integer id){
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
package org.example.aces_api.service;

import org.example.aces_api.controller.AreaController; // Importar o controlador para referenciar os métodos
import org.example.aces_api.dto.AreaCreateDto;
import org.example.aces_api.dto.AreaResponseDto;
import org.example.aces_api.exception.EntityNotFoundException;
import org.example.aces_api.mapper.AreaMapper;
import org.example.aces_api.model.entity.Area;
import org.example.aces_api.model.repository.AreaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.hateoas.EntityModel; // Import para EntityModel
import org.springframework.hateoas.CollectionModel; // Import para CollectionModel

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors; // Import para Collectors (se ainda não tiver)

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo; // Import estático
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn; // Import estático

@Service
public class AreaService {

    @Autowired
    private AreaRepository repository;

    @Autowired
    private AreaMapper mapper;

    // Tipo de retorno alterado para EntityModel<AreaResponseDto>
    public EntityModel<AreaResponseDto> findById(Integer id) {
        var area = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Area com ID " + id + " não encontrada."));
        AreaResponseDto dto = mapper.toDto(area);
        // Cria um EntityModel e adiciona o link de auto-referência
        return EntityModel.of(dto,
                linkTo(methodOn(AreaController.class).getAreaById(id)) // <-- ALTERADO PARA getAreaById
                        .withSelfRel());
    }

    // Tipo de retorno alterado para EntityModel<AreaResponseDto>
    public EntityModel<AreaResponseDto> criarArea(AreaCreateDto areaCreate){
        var entity = mapper.toEntity(areaCreate);
        entity.setDataUltimaAtt(LocalDateTime.now());
        var area = repository.save(entity);

        AreaResponseDto dto = mapper.toDto(area);
        // Cria um EntityModel e adiciona o link para o recurso recém-criado
        return EntityModel.of(dto,
                linkTo(methodOn(AreaController.class).getAreaById(dto.id())).withSelfRel());
    }

    // Tipo de retorno alterado para EntityModel<AreaResponseDto>
    // Conforme solicitado anteriormente, este método não adiciona um link de auto-referência ao próprio recurso atualizado.
    public EntityModel<AreaResponseDto> atualizarArea(Integer id, AreaCreateDto areaCreate){
        var entity = repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Area com ID " + id + " não encontrada."));

        entity.setNome(areaCreate.nome());
        entity.setDescricao(areaCreate.descricao());
        entity.setRegiao(areaCreate.regiao());
        entity.setPopulacaoAprox(areaCreate.populacaoAprox());
        entity.setNivelRisco(areaCreate.nivelRisco());
        entity.setPrioridade(areaCreate.prioridade());
        entity.setDataUltimaAtt(LocalDateTime.now());

        var area = repository.save(entity);
        AreaResponseDto dto = mapper.toDto(area);
        // Retorna o EntityModel sem adicionar link Self, como solicitado anteriormente
        return EntityModel.of(dto);
    }

    // Tipo de retorno alterado para CollectionModel<EntityModel<AreaResponseDto>>
    public CollectionModel<EntityModel<AreaResponseDto>> findAll(){
        // Mapeia cada Area para AreaResponseDto, então encapsula em EntityModel com link Self
        List<EntityModel<AreaResponseDto>> areasComLinks = repository.findAll().stream()
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
        var area = repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Area com ID " + id + " não encontrada."));
        repository.delete(area);
    }
}
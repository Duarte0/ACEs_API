package org.example.aces_api.service;

import org.example.aces_api.controller.EnderecoController; // Importar o controlador para referenciar métodos
import org.example.aces_api.dto.EnderecoCreateDto;
import org.example.aces_api.dto.EnderecoResponseDto;
import org.example.aces_api.exception.EntityNotFoundException;
import org.example.aces_api.mapper.EnderecoMapper;
import org.example.aces_api.model.entity.Area; // Importar a entidade Area
import org.example.aces_api.model.entity.Endereco;
import org.example.aces_api.model.repository.EnderecoRepository;
import org.example.aces_api.model.repository.AreaRepository; // Importar AreaRepository

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class EnderecoService {

    @Autowired
    private EnderecoRepository repository;

    @Autowired
    private EnderecoMapper mapper;

    @Autowired
    private AreaRepository areaRepository; // Para buscar a entidade Area

    public EntityModel<EnderecoResponseDto> findById(Integer id) {
        var endereco = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Endereço com ID " + id + " não encontrado."));
        EnderecoResponseDto dto = mapper.toDto(endereco);
        return EntityModel.of(dto,
                linkTo(methodOn(EnderecoController.class).getEnderecoById(id)).withSelfRel()
        );
    }

    public EntityModel<EnderecoResponseDto> criarEndereco(EnderecoCreateDto enderecoCreateDto) {
        // Busca a entidade Area primeiro para associar ao Endereco
        Area area = areaRepository.findById(enderecoCreateDto.area_id())
                .orElseThrow(() -> new EntityNotFoundException("Área com ID " + enderecoCreateDto.area_id() + " não encontrada."));

        Endereco entity = mapper.toEntity(enderecoCreateDto);
        entity.setArea(area); // Associa a entidade Area ao Endereco

        var endereco = repository.save(entity);
        EnderecoResponseDto dto = mapper.toDto(endereco);
        return EntityModel.of(dto,
                linkTo(methodOn(EnderecoController.class).getEnderecoById(dto.id())).withSelfRel()
        );
    }

    public EntityModel<EnderecoResponseDto> atualizarEndereco(Integer id, EnderecoCreateDto enderecoCreateDto) {
        var entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Endereço com ID " + id + " não encontrado."));

        // Busca e associa a entidade Area novamente em caso de atualização de area_id
        Area area = areaRepository.findById(enderecoCreateDto.area_id())
                .orElseThrow(() -> new EntityNotFoundException("Área com ID " + enderecoCreateDto.area_id() + " não encontrada."));
        entity.setArea(area);

        // Atualiza os outros campos do Endereço
        entity.setLogradouro(enderecoCreateDto.logradouro());
        entity.setNumero(enderecoCreateDto.numero());
        entity.setBairro(enderecoCreateDto.bairro());
        entity.setCep(enderecoCreateDto.cep());
        entity.setLatitude(enderecoCreateDto.latitude());
        entity.setLongitude(enderecoCreateDto.longitude());
        entity.setSpolmovel(enderecoCreateDto.spolmovel());

        var endereco = repository.save(entity);
        EnderecoResponseDto dto = mapper.toDto(endereco);
        return EntityModel.of(dto);
    }

    public CollectionModel<EntityModel<EnderecoResponseDto>> findAll() {
        List<EntityModel<EnderecoResponseDto>> enderecosComLinks = repository.findAll().stream()
                .map(endereco -> {
                    EnderecoResponseDto dto = mapper.toDto(endereco);
                    return EntityModel.of(dto,
                            linkTo(methodOn(EnderecoController.class).getEnderecoById(dto.id())).withSelfRel()
                    );
                })
                .collect(Collectors.toList());

        return CollectionModel.of(enderecosComLinks,
                linkTo(methodOn(EnderecoController.class).getAllEnderecos()).withSelfRel());
    }

    public void excluirEndereco(Integer id) {
        var endereco = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Endereço com ID " + id + " não encontrado."));
        repository.delete(endereco);
    }
}
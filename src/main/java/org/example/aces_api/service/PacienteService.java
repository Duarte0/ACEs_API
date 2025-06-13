package org.example.aces_api.service;

import org.example.aces_api.controller.PacienteController;
import org.example.aces_api.dto.PacienteCreateDto;
import org.example.aces_api.dto.PacienteResponseDto;
import org.example.aces_api.exception.EntityNotFoundException;
import org.example.aces_api.mapper.PacienteMapper;
import org.example.aces_api.model.entity.Endereco;
import org.example.aces_api.model.repository.PacienteRepository;
import org.example.aces_api.model.repository.EnderecoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.CollectionModel;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class PacienteService {

    @Autowired
    private PacienteRepository repository;

    @Autowired
    private PacienteMapper mapper;

    @Autowired
    private EnderecoRepository enderecoRepository;

    public EntityModel<PacienteResponseDto> findById(Integer id) {
        var paciente = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Paciente com ID " + id + " não encontrado."));
        PacienteResponseDto dto = mapper.toDto(paciente);
        return EntityModel.of(dto,
                linkTo(methodOn(PacienteController.class).getPacienteById(id)).withSelfRel()
        );
    }

    public EntityModel<PacienteResponseDto> criarPaciente(PacienteCreateDto pacienteCreateDto){
        var entity = mapper.toEntity(pacienteCreateDto);


        Endereco endereco = enderecoRepository.findById(pacienteCreateDto.endereco_id())
                .orElseThrow(() -> new EntityNotFoundException("Endereço com ID " + pacienteCreateDto.endereco_id() + " não encontrado."));
        entity.setEndereco(endereco);

        var paciente = repository.save(entity);

        PacienteResponseDto dto = mapper.toDto(paciente);
        return EntityModel.of(dto,
                linkTo(methodOn(PacienteController.class).getPacienteById(dto.id())).withSelfRel()
        );
    }

    public EntityModel<PacienteResponseDto> atualizarPaciente(Integer id, PacienteCreateDto pacienteCreateDto){
        var entity = repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Paciente com ID " + id + " não encontrado."));


        Endereco endereco = enderecoRepository.findById(pacienteCreateDto.endereco_id())
                .orElseThrow(() -> new EntityNotFoundException("Endereço com ID " + pacienteCreateDto.endereco_id() + " não encontrado."));
        entity.setEndereco(endereco);

        entity.setNome(pacienteCreateDto.nome());
        entity.setDataNascimento(pacienteCreateDto.dataNascimento());
        entity.setSexo(pacienteCreateDto.sexo());
        entity.setCpf(pacienteCreateDto.cpf());
        entity.setCartaoSUS(pacienteCreateDto.cartaoSUS());
        entity.setTelefone(pacienteCreateDto.telefone());

        var paciente = repository.save(entity);
        PacienteResponseDto dto = mapper.toDto(paciente);
        return EntityModel.of(dto);
    }

    public CollectionModel<EntityModel<PacienteResponseDto>> findAll(){
        List<EntityModel<PacienteResponseDto>> pacientesComLinks = repository.findAll().stream()
                .map(paciente -> {
                    PacienteResponseDto dto = mapper.toDto(paciente);
                    return EntityModel.of(dto,
                            linkTo(methodOn(PacienteController.class).getPacienteById(dto.id())).withSelfRel()
                    );
                })
                .collect(Collectors.toList());

        return CollectionModel.of(pacientesComLinks,
                linkTo(methodOn(PacienteController.class).getAllPacientes()).withSelfRel());
    }

    public void excluirPaciente(Integer id){
        var paciente = repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Paciente com ID " + id + " não encontrado."));
        repository.delete(paciente);
    }
}
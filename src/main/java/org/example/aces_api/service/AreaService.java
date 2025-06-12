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

}

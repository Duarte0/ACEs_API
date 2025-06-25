package org.example.aces_api.service;


import org.example.aces_api.dto.VisitaCreateDto;
import org.example.aces_api.dto.VisitaResponseDto;
import org.example.aces_api.exception.ResourceNotFoundException;
import org.example.aces_api.mapper.VisitaMapper;
import org.example.aces_api.model.entity.Agente;
import org.example.aces_api.model.entity.Area;
import org.example.aces_api.model.entity.Endereco;
import org.example.aces_api.model.entity.Visita;
import org.example.aces_api.model.repository.AgenteRepository;
import org.example.aces_api.model.repository.EnderecoRepository;
import org.example.aces_api.model.repository.VisitaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class VisitaService {

    private final VisitaRepository visitaRepository;
    private final AgenteRepository agenteRepository;
    private final EnderecoRepository enderecoRepository;
    private final VisitaMapper visitaMapper;

    @Autowired
    public VisitaService(VisitaRepository visitaRepository,
                         AgenteRepository agenteRepository,
                         EnderecoRepository enderecoRepository,
                         VisitaMapper visitaMapper) {
        this.visitaRepository = visitaRepository;
        this.agenteRepository = agenteRepository;
        this.enderecoRepository = enderecoRepository;
        this.visitaMapper = visitaMapper;
    }

    @Transactional
    public VisitaResponseDto registrarVisita(VisitaCreateDto visitaDto) {
        Agente agente = agenteRepository.findById(visitaDto.agenteId())
                .orElseThrow(() -> new ResourceNotFoundException("Agente não encontrado com ID: " + visitaDto.agenteId()));

        Endereco endereco = enderecoRepository.findById(visitaDto.enderecoId())
                .orElseThrow(() -> new ResourceNotFoundException("Endereço não encontrado com ID: " + visitaDto.enderecoId()));

        Visita visita = new Visita();
        visita.setAgente(agente);
        visita.setEndereco(endereco);
        visita.setDataHora(visitaDto.dataHora());
        visita.setObservacoes(visitaDto.observacoes());
        visita.setStatus(visitaDto.status());
        visita.setFoiRealizada(visitaDto.foiRealizada());
        Visita visitaSalva = visitaRepository.save(visita);
        return visitaMapper.toDto(visitaSalva);
    }


    public Page<VisitaResponseDto> listarVisitas(Pageable pageable) {
        Page<Visita> visitas = visitaRepository.findAll(pageable);
        return visitas.map(visita -> visitaMapper.toDto(visita));
    }


    public List<VisitaResponseDto> listarVisitasPorAgente(Long agenteId) {
        List<Visita> visitas = visitaRepository.findByAgenteId(agenteId);
        return visitaMapper.toDto(visitas);
    }

    public List<VisitaResponseDto> listarVisitasPorEndereco(Long enderecoId) {
        List<Visita> visitas = visitaRepository.findByEnderecoId(enderecoId);
        return visitaMapper.toDto(visitas);
    }

    public Optional<VisitaResponseDto> buscarVisitaPorId(Long id) {
        return visitaRepository.findById(id)
                .map(visitaMapper::toDto);
    }

    @Transactional
    public VisitaResponseDto atualizarStatus(Long id, String novoStatus) {
        Visita visita = visitaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Visita não encontrada com ID: " + id));

        visita.setStatus(novoStatus);
        Visita visitaAtualizada = visitaRepository.save(visita);

        return visitaMapper.toDto(visitaAtualizada);
    }

    @Transactional
    public VisitaResponseDto marcarComoRealizada(Long id, boolean foiRealizada) {
        Visita visita = visitaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Visita não encontrada com ID: " + id));

        visita.setFoiRealizada(foiRealizada);
        Visita visitaAtualizada = visitaRepository.save(visita);

        return visitaMapper.toDto(visitaAtualizada);
    }

    @Transactional
    public VisitaResponseDto atualizarObservacoes(Long id, String observacoes) {
        Visita visita = visitaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Visita não encontrada com ID: " + id));

        visita.setObservacoes(observacoes);
        Visita visitaAtualizada = visitaRepository.save(visita);

        return visitaMapper.toDto(visitaAtualizada);
    }

    @Transactional
    public boolean excluirVisita(Long id) {
        if (!visitaRepository.existsById(id)) {
            throw new ResourceNotFoundException("Visita não encontrada com ID: " + id);
        }
        visitaRepository.deleteById(id);
        return false;
    }
}
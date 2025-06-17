package org.example.aces_api.service;

import org.example.aces_api.dto.CasoDengueConfirmacaoLabDto;
import org.example.aces_api.dto.CasoDengueCreateDto;
import org.example.aces_api.dto.CasoDengueResponseDto;
import org.example.aces_api.dto.CasoDengueUpdateDto;
import org.example.aces_api.exception.ResourceNotFoundException;
import org.example.aces_api.mapper.CasoDengueMapper;
import org.example.aces_api.model.entity.CasoDengue;
import org.example.aces_api.model.entity.Paciente;
import org.example.aces_api.model.entity.Visita;
import org.example.aces_api.model.repository.CasoDengueRepository;
import org.example.aces_api.model.repository.PacienteRepository;
import org.example.aces_api.model.repository.VisitaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CasoDengueService {

    private final CasoDengueRepository casoDengueRepository;
    private final VisitaRepository visitaRepository;
    private final PacienteRepository pacienteRepository;
    private final CasoDengueMapper casoDengueMapper;

    @Autowired
    public CasoDengueService(CasoDengueRepository casoDengueRepository,
                             VisitaRepository visitaRepository,
                             PacienteRepository pacienteRepository,
                             CasoDengueMapper casoDengueMapper) {
        this.casoDengueRepository = casoDengueRepository;
        this.visitaRepository = visitaRepository;
        this.pacienteRepository = pacienteRepository;
        this.casoDengueMapper = casoDengueMapper;
    }

    @Transactional
    public CasoDengueResponseDto registrarCasoDengue(CasoDengueCreateDto dto) {

        Visita visita = visitaRepository.findById(dto.visitaId())
                .orElseThrow(() -> new ResourceNotFoundException("Visita não encontrada com ID: " + dto.visitaId()));


        Paciente paciente = pacienteRepository.findById(dto.pacienteId())
                .orElseThrow(() -> new ResourceNotFoundException("Paciente não encontrado com ID: " + dto.pacienteId()));


        CasoDengue casoDengue = new CasoDengue();
        casoDengue.setVisita(visita);
        casoDengue.setPaciente(paciente);
        casoDengue.setDataDiagnostico(dto.dataDiagnostico());
        casoDengue.setTipoDengue(String.valueOf(dto.tipoDengue()));
        casoDengue.setGravidade(dto.gravidade());
        casoDengue.setSintomas(dto.sintomas());
        casoDengue.setObservacoes(dto.observacoes());
        casoDengue.setConfirmadoLab(dto.confirmadoLab());

        CasoDengue casoSalvo;
        casoSalvo = casoDengueRepository.save(casoDengue);

        return casoDengueMapper.toDto(casoSalvo);
    }

    public Page<CasoDengueResponseDto> listarCasosDengue(Pageable pageable) {
        Page<CasoDengue> casos = casoDengueRepository.findAll(pageable);
        return casos.map(casoDengueMapper::toDto);
    }

    public List<CasoDengueResponseDto> listarCasosPorPaciente(Long pacienteId) {
        List<CasoDengue> casos = casoDengueRepository.findByPacienteId(pacienteId);
        return casoDengueMapper.toDto(casos);
    }

    public List<CasoDengueResponseDto> listarCasosPorVisita(Long visitaId) {
        List<CasoDengue> casos = casoDengueRepository.findByVisitaId(visitaId);
        return casoDengueMapper.toDto(casos);
    }

    public CasoDengueResponseDto buscarCasoDenguePorId(Long id) {
        CasoDengue caso = casoDengueRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Caso de dengue não encontrado com ID: " + id));
        return casoDengueMapper.toDto(caso);
    }

    @Transactional
    public CasoDengueResponseDto atualizarCasoDengue(Long id, CasoDengueUpdateDto dto) {
        CasoDengue casoDengue = casoDengueRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Caso de dengue não encontrado com ID: " + id));

        if (dto.dataDiagnostico() != null) {
            casoDengue.setDataDiagnostico(dto.dataDiagnostico());
        }

        if (dto.tipoDengue() != null) {
            casoDengue.setTipoDengue(dto.tipoDengue());
        }

        if (dto.gravidade() != null) {
            casoDengue.setGravidade(dto.gravidade());
        }

        if (dto.sintomas() != null) {
            casoDengue.setSintomas(dto.sintomas());
        }

        if (dto.observacoes() != null) {
            casoDengue.setObservacoes(dto.observacoes());
        }

        CasoDengue casoAtualizado = casoDengueRepository.save(casoDengue);

        return casoDengueMapper.toDto(casoAtualizado);
    }

    @Transactional
    public CasoDengueResponseDto confirmarLaboratorio(Long id, CasoDengueConfirmacaoLabDto dto) {
        CasoDengue casoDengue = casoDengueRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Caso de dengue não encontrado com ID: " + id));

        casoDengue.setConfirmadoLab(dto.confirmadoLab());

        if (dto.observacoes() != null && !dto.observacoes().isEmpty()) {
            String observacoesAtuais = casoDengue.getObservacoes();
            String novasObservacoes = observacoesAtuais != null && !observacoesAtuais.isEmpty()
                    ? observacoesAtuais + "\n[Confirmação Lab]: " + dto.observacoes()
                    : "[Confirmação Lab]: " + dto.observacoes();

            casoDengue.setObservacoes(novasObservacoes);
        }

        CasoDengue casoAtualizado = casoDengueRepository.save(casoDengue);

        return casoDengueMapper.toDto(casoAtualizado);
    }

    @Transactional
    public void excluirCasoDengue(Long id) {
        if (!casoDengueRepository.existsById(id)) {
            throw new ResourceNotFoundException("Caso de dengue não encontrado com ID: " + id);
        }
        casoDengueRepository.deleteById(id);
    }
}
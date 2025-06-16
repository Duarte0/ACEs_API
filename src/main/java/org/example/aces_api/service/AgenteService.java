package org.example.aces_api.service;

import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import org.example.aces_api.dto.AgenteRequestDTO;
import org.example.aces_api.dto.AgenteResponseDTO;
import org.example.aces_api.dto.AgenteUpdateDTO;
import org.example.aces_api.exception.DuplicatedResourceException;
import org.example.aces_api.exception.EntityNotFoundException;
import org.example.aces_api.mapper.AgenteMapper;
import org.example.aces_api.model.entity.Agente;
import org.example.aces_api.model.entity.AgenteArea;
import org.example.aces_api.model.entity.AgenteAreaId;
import org.example.aces_api.model.entity.Area;
import org.example.aces_api.model.repository.AgenteAreaRepository;
import org.example.aces_api.model.repository.AgenteRepository;
import org.example.aces_api.model.repository.AreaRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AgenteService {

    private final AgenteRepository agenteRepository;
    private final AgenteMapper agenteMapper;
    private final AreaRepository areaRepository;
    private final AgenteAreaRepository agenteAreaRepository;


    public AgenteService(AgenteRepository agenteRepository, AgenteMapper agenteMapper, AreaRepository areaRepository, AgenteAreaRepository agenteAreaRepository) {
        this.agenteRepository = agenteRepository;
        this.agenteMapper = agenteMapper;
        this.areaRepository = areaRepository;
        this.agenteAreaRepository = agenteAreaRepository;
    }

    @Transactional
    public AgenteResponseDTO criarAgente(AgenteRequestDTO agenteRequest) {
        validarAgenteAntesDeCriar(agenteRequest);

        Agente agente = agenteMapper.requestToEntity(agenteRequest);
        agente.setAtivo(true);

        Agente agenteSalvo = agenteRepository.save(agente);
        return agenteMapper.toResponseDTO(agenteSalvo);
    }

    public AgenteResponseDTO buscarPorId(Integer id) {
        Agente agente = agenteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Agente não encontrado com ID: " + id));
        return agenteMapper.toResponseDTO(agente);
    }

    public List<AgenteResponseDTO> listarTodos() {
        return agenteRepository.findAll().stream()
                .map(agenteMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public AgenteResponseDTO atualizarAgente(Integer id, AgenteUpdateDTO agenteUpdateDTO) {
        Agente agenteExistente = agenteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Agente não encontrado com ID: " + id));

        validarAtualizacao(agenteExistente, agenteUpdateDTO);

        if (agenteUpdateDTO.matricula() != null) {
            agenteExistente.setMatricula(agenteUpdateDTO.matricula());
        }

        if (agenteUpdateDTO.nome() != null) {
            agenteExistente.setNome(agenteUpdateDTO.nome());
        }

        // Atualiza datas se fornecidas
        agenteExistente.setDataAdmissao(parseDate(agenteUpdateDTO.dataAdmissao()));
        agenteExistente.setDataInicio(parseDate(agenteUpdateDTO.dataInicio()));
        agenteExistente.setDataFim(parseDate(agenteUpdateDTO.dataFim()));

        if (agenteUpdateDTO.ativo() != null) {
            agenteExistente.setAtivo(agenteUpdateDTO.ativo());
        }

        Agente agenteAtualizado = agenteRepository.save(agenteExistente);
        return agenteMapper.toResponseDTO(agenteAtualizado);
    }

    @Transactional
    public void desativarAgente(Integer id) {
        Agente agente = agenteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Agente não encontrado com ID: " + id));
        agente.setAtivo(false);
        agenteRepository.save(agente);
    }

    @Transactional
    public void ativarAgente(Integer id) {
        Agente agente = agenteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Agente não encontrado com ID: " + id));
        agente.setAtivo(true);
        agenteRepository.save(agente);
    }

    private void validarAgenteAntesDeCriar(AgenteRequestDTO dto) {
        if (agenteRepository.existsByMatricula(dto.matricula())) {
            throw new DuplicatedResourceException("Matrícula já cadastrada: " + dto.matricula());
        }
    }

    private void validarAtualizacao(Agente existente, AgenteUpdateDTO atualizado) {
        if (atualizado.matricula() != null && !existente.getMatricula().equals(atualizado.matricula())) {
            if (agenteRepository.existsByMatricula(atualizado.matricula())) {
                throw new DuplicatedResourceException("Matrícula já em uso: " + atualizado.matricula());
            }
        }
    }

    public AgenteResponseDTO buscarPorMatricula(String matricula) {
        Agente agente = agenteRepository.findByMatricula(matricula)
                .orElseThrow(() -> new EntityNotFoundException("Agente não encontrado com matrícula: " + matricula));
        return agenteMapper.toResponseDTO(agente);
    }

    public List<AgenteResponseDTO> buscarPorNome(String nome) {
        return agenteRepository.findByNomeContainingIgnoreCase(nome).stream()
                .map(agenteMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    private LocalDate parseDate(String dateString) {
        return dateString != null ? LocalDate.parse(dateString) : null;
    }

    @Transactional
    public void associarArea(Integer agenteId, Integer areaId) {
        Agente agente = agenteRepository.findById(agenteId)
                .orElseThrow(() -> new EntityNotFoundException("Agente não encontrado com ID: " + agenteId));

        Area area = areaRepository.findById(areaId)
                .orElseThrow(() -> new EntityNotFoundException("Área não encontrada com ID: " + areaId));

        if (agenteAreaRepository.existsById(new AgenteAreaId(agenteId, areaId))) {
            throw new IllegalStateException("Esta área já está associada ao agente");
        }

        AgenteArea agenteArea = new AgenteArea(agente, area);
        agenteAreaRepository.save(agenteArea);
    }

    public List<Area> listarAreasPorAgente(Integer agenteId) {
        return agenteAreaRepository.findByAgenteId(agenteId)
                .stream()
                .map(AgenteArea::getArea)
                .collect(Collectors.toList());
    }

    @Transactional
    public void removerArea(Integer agenteId, Integer areaId) {
        if (!agenteAreaRepository.existsById(new AgenteAreaId(agenteId, areaId))) {
            throw new EntityNotFoundException("Associação não encontrada");
        }
        agenteAreaRepository.deleteById(new AgenteAreaId(agenteId, areaId));
    }
}
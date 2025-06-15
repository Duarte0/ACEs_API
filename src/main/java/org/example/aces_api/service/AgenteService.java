package org.example.aces_api.service;

import jakarta.transaction.Transactional;
import org.example.aces_api.dto.AgenteRequestDTO;
import org.example.aces_api.dto.AgenteResponseDTO;
import org.example.aces_api.dto.AgenteUpdateDTO;
import org.example.aces_api.exception.DuplicatedResourceException;
import org.example.aces_api.exception.EntityNotFoundException;
import org.example.aces_api.mapper.AgenteMapper;
import org.example.aces_api.model.entity.Agente;
import org.example.aces_api.model.entity.Usuario;
import org.example.aces_api.model.repository.AgenteRepository;
import org.example.aces_api.model.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AgenteService {

    private final AgenteRepository agenteRepository;
    private final UsuarioRepository usuarioRepository;
    private final AgenteMapper agenteMapper;

    public AgenteService(AgenteRepository agenteRepository,
                         UsuarioRepository usuarioRepository,
                         AgenteMapper agenteMapper) {
        this.agenteRepository = agenteRepository;
        this.usuarioRepository = usuarioRepository;
        this.agenteMapper = agenteMapper;
    }

    @Transactional
    public AgenteResponseDTO criarAgente(AgenteRequestDTO agenteRequest) {
        validarAgenteAntesDeCriar(agenteRequest);

        Usuario usuario = usuarioRepository.findById(agenteRequest.usuarioId())
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado com ID: " + agenteRequest.usuarioId()));

        Agente agente = agenteMapper.requestToEntity(agenteRequest);
        agente.setUsuario(usuario);

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

        if (agenteRepository.existsByUsuarioId(dto.usuarioId())) {
            throw new DuplicatedResourceException("Usuário já vinculado a outro agente");
        }
    }

    private void validarAtualizacao(Agente existente, AgenteUpdateDTO atualizado) {
        if (atualizado.matricula() != null && !existente.getMatricula().equals(atualizado.matricula())) {
            if (agenteRepository.existsByMatricula(atualizado.matricula())) {
                throw new DuplicatedResourceException("Matrícula já em uso: " + atualizado.matricula());
            }
        }
    }

    private LocalDate parseDate(String dateString) {
        return dateString != null ? LocalDate.parse(dateString) : null;
    }

}
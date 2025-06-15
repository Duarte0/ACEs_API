package org.example.aces_api.service;

import jakarta.transaction.Transactional;
import org.example.aces_api.dto.UsuarioRequestDTO;
import org.example.aces_api.dto.UsuarioResponseDTO;
import org.example.aces_api.dto.UsuarioUpdateDTO;
import org.example.aces_api.exception.DuplicatedResourceException;
import org.example.aces_api.exception.EntityNotFoundException;
import org.example.aces_api.mapper.UsuarioMapper;
import org.example.aces_api.model.entity.Usuario;
import org.example.aces_api.model.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;

    public UsuarioService(UsuarioRepository usuarioRepository, UsuarioMapper usuarioMapper) {
        this.usuarioRepository = usuarioRepository;
        this.usuarioMapper = usuarioMapper;
    }

    // Criação com RequestDTO
    @Transactional
    public UsuarioResponseDTO criarUsuario(UsuarioRequestDTO usuarioRequest) {
        validarUsuarioAntesDeCriar(usuarioRequest);

        Usuario usuario = usuarioMapper.requestToEntity(usuarioRequest);
        usuario.setAtivo(true);
        usuario.setDataCriacao(LocalDateTime.now());

        Usuario usuarioSalvo = usuarioRepository.save(usuario);
        return usuarioMapper.toResponseDTO(usuarioSalvo);
    }

    // Busca por ID
    public UsuarioResponseDTO buscarPorId(Integer id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado com ID: " + id));
        return usuarioMapper.toResponseDTO(usuario);
    }


    // Listagem
    public List<UsuarioResponseDTO> listarTodos() {
        return usuarioRepository.findAll().stream()
                .map(usuarioMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    // Atualização com UpdateDTO
    @Transactional
    public UsuarioResponseDTO atualizarUsuario(Integer id, UsuarioUpdateDTO usuarioUpdateDTO) {
        Usuario usuarioExistente = usuarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado com ID: " + id));

        validarAtualizacao(usuarioExistente, usuarioUpdateDTO);

        if (usuarioUpdateDTO.nome() != null) {
            usuarioExistente.setNome(usuarioUpdateDTO.nome());
        }
        if (usuarioUpdateDTO.telefone() != null) {
            usuarioExistente.setTelefone(usuarioUpdateDTO.telefone());
        }
        if (usuarioUpdateDTO.cargo() != null) {
            usuarioExistente.setCargo(usuarioUpdateDTO.cargo());
        }

        usuarioExistente.setUltimoAcesso(LocalDateTime.now());
        Usuario usuarioAtualizado = usuarioRepository.save(usuarioExistente);
        return usuarioMapper.toResponseDTO(usuarioAtualizado);
    }

    // Desativação
    @Transactional
    public void desativarUsuario(Integer id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado com ID: " + id));

        usuario.setAtivo(false);
        usuarioRepository.save(usuario);
    }

    // Validações
    private void validarUsuarioAntesDeCriar(UsuarioRequestDTO dto) {
        if (usuarioRepository.existsByCpf(dto.cpf())) {
            throw new DuplicatedResourceException("CPF já cadastrado: " + dto.cpf());
        }

        if (usuarioRepository.existsByEmail(dto.email())) {
            throw new DuplicatedResourceException("Email já cadastrado: " + dto.email());
        }
    }

    private void validarAtualizacao(Usuario existente, UsuarioUpdateDTO atualizado) {
        if (atualizado.email() != null && !existente.getEmail().equals(atualizado.email())) {
            if (usuarioRepository.existsByEmail(atualizado.email())) {
                throw new DuplicatedResourceException("Email já está em uso: " + atualizado.email());
            }
        }
    }

}
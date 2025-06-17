package org.example.aces_api.service;

import org.example.aces_api.dto.FocoAedesCreateDto;
import org.example.aces_api.dto.FocoAedesImagemDto;
import org.example.aces_api.dto.FocoAedesResponseDto;
import org.example.aces_api.dto.FocoAedesTratamentoDto;
import org.example.aces_api.exception.EntityNotFoundException;
import org.example.aces_api.exception.ResourceNotFoundException;
import org.example.aces_api.mapper.FocoAedesMapper;
import org.example.aces_api.model.entity.FocoAedes;
import org.example.aces_api.model.entity.Visita;
import org.example.aces_api.model.repository.FocoAedesRepository;
import org.example.aces_api.model.repository.VisitaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class FocoAedesService {

    private final FocoAedesRepository focoAedesRepository;
    private final VisitaRepository visitaRepository;
    private final FocoAedesMapper focoAedesMapper;
    private final ArmazenamentoImagemService armazenamentoImagemService;

    @Autowired
    public FocoAedesService(FocoAedesRepository focoAedesRepository,
                            VisitaRepository visitaRepository,
                            FocoAedesMapper focoAedesMapper,
                            ArmazenamentoImagemService armazenamentoImagemService) {
        this.focoAedesRepository = focoAedesRepository;
        this.visitaRepository = visitaRepository;
        this.focoAedesMapper = focoAedesMapper;
        this.armazenamentoImagemService = armazenamentoImagemService;
    }

    @Transactional
    public FocoAedesResponseDto registrarFocoAedes(FocoAedesCreateDto dto) {
        Visita visita = visitaRepository.findById(dto.visitaId())
                .orElseThrow(() -> new ResourceNotFoundException("Visita não encontrada com ID: " + dto.visitaId()));

        FocoAedes focoAedes = new FocoAedes();
        focoAedes.setVisita(visita);
        focoAedes.setImagem(dto.imagem());
        focoAedes.setTipoFoco(String.valueOf(dto.tipoFoco()));
        focoAedes.setQuantidade(dto.quantidade());
        focoAedes.setTratado(dto.tratado());
        focoAedes.setObservacoes(dto.observacoes());

        FocoAedes focoSalvo = focoAedesRepository.save(focoAedes);

        return focoAedesMapper.toDto(focoSalvo);
    }

    public Page<FocoAedesResponseDto> listarFocosAedes(Pageable pageable) {
        Page<FocoAedes> focos = focoAedesRepository.findAll(pageable);
        return focos.map(focoAedesMapper::toDto);
    }

    public List<FocoAedesResponseDto> listarFocosPorVisita(Long visitaId) {
        List<FocoAedes> focos = focoAedesRepository.findByVisitaId(visitaId);
        return focoAedesMapper.toDto(focos);
    }

    public List<FocoAedesResponseDto> listarFocosPorTipo(String tipoFoco) {
        List<FocoAedes> focos = focoAedesRepository.findByTipoFoco(tipoFoco);
        return focoAedesMapper.toDto(focos);
    }

    public List<FocoAedesResponseDto> listarFocosPorStatusTratamento(boolean tratado) {
        List<FocoAedes> focos = focoAedesRepository.findByTratado(tratado);
        return focoAedesMapper.toDto(focos);
    }

    public FocoAedesResponseDto buscarFocoAedesPorId(Long id) {
        FocoAedes foco = focoAedesRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Foco de Aedes não encontrado com ID: " + id));
        return focoAedesMapper.toDto(foco);
    }

    @Transactional
    public FocoAedesResponseDto atualizarFocoAedes(Long id, FocoAedesCreateDto dto) {
        // Buscar o foco de Aedes pelo ID
        FocoAedes focoAedes = focoAedesRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Foco de Aedes não encontrado com ID: " + id));

        if (dto.imagem() != null) {
            focoAedes.setImagem(dto.imagem());
        }

        if (dto.tipoFoco() != null) {
            focoAedes.setTipoFoco(String.valueOf(dto.tipoFoco()));
        }

        focoAedes.setQuantidade(dto.quantidade());
        focoAedes.setTratado(dto.tratado());

        if (dto.observacoes() != null) {
            focoAedes.setObservacoes(dto.observacoes());
        }

        FocoAedes focoAtualizado = focoAedesRepository.save(focoAedes);

        return focoAedesMapper.toDto(focoAtualizado);
    }

    @Transactional
    public FocoAedesResponseDto atualizarTratamento(Long id, FocoAedesTratamentoDto dto) {
        // Buscar o foco de Aedes pelo ID
        FocoAedes focoAedes = focoAedesRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Foco de Aedes não encontrado com ID: " + id));

        focoAedes.setTratado(dto.tratado());

        if (dto.observacoes() != null && !dto.observacoes().isEmpty()) {
            String observacoesAtuais = focoAedes.getObservacoes();
            String novasObservacoes = observacoesAtuais != null && !observacoesAtuais.isEmpty()
                    ? observacoesAtuais + "\n[Tratamento]: " + dto.observacoes()
                    : "[Tratamento]: " + dto.observacoes();

            focoAedes.setObservacoes(novasObservacoes);
        }

        FocoAedes focoAtualizado = focoAedesRepository.save(focoAedes);

        return focoAedesMapper.toDto(focoAtualizado);
    }

    @Transactional
    public FocoAedesResponseDto anexarImagem(Long id, FocoAedesImagemDto dto) {
        // Buscar o foco de Aedes pelo ID
        FocoAedes focoAedes = focoAedesRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Foco de Aedes não encontrado com ID: " + id));

        focoAedes.setImagem(dto.imagem());

        FocoAedes focoAtualizado = focoAedesRepository.save(focoAedes);

        return focoAedesMapper.toDto(focoAtualizado);
    }

    /**
     * Método para anexar uma imagem ao foco usando MultipartFile
     */
    @Transactional
    public FocoAedesResponseDto anexarImagem(Long id, MultipartFile imagem) {
        // Buscar o foco de Aedes pelo ID
        FocoAedes focoAedes = focoAedesRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Foco de Aedes não encontrado com ID: " + id));

        // Converter a imagem para Base64
        String imagemBase64 = armazenamentoImagemService.converterImagemParaBase64(imagem);

        // Salvar a imagem como Base64 no foco
        focoAedes.setImagem(imagemBase64);

        // Opcionalmente, se você quiser armazenar também o arquivo físico
        // String nomeArquivo = armazenamentoImagemService.armazenarImagem(imagem);
        // focoAedes.setNomeArquivoImagem(nomeArquivo);

        FocoAedes focoAtualizado = focoAedesRepository.save(focoAedes);

        return focoAedesMapper.toDto(focoAtualizado);
    }

    @Transactional
    public void excluirFocoAedes(Long id) {
        if (!focoAedesRepository.existsById(id)) {
            throw new ResourceNotFoundException("Foco de Aedes não encontrado com ID: " + id);
        }
        focoAedesRepository.deleteById(id);
    }

    public String obterImagemFoco(Long id) {
        FocoAedes foco = focoAedesRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Foco não encontrado com ID: " + id));
        return foco.getImagem();
    }
}

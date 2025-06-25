package org.example.aces_api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.example.aces_api.dto.FocoAedesCreateDto;
import org.example.aces_api.dto.FocoAedesImagemDto;
import org.example.aces_api.dto.FocoAedesResponseDto;
import org.example.aces_api.dto.FocoAedesTratamentoDto;
import org.example.aces_api.service.FocoAedesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/focos-aedes")
@Tag(name = "Focos de Aedes", description = "API para gerenciamento de focos do mosquito Aedes aegypti")
public class FocoAedesController {

    private final FocoAedesService focoAedesService;

    @Autowired
    public FocoAedesController(FocoAedesService focoAedesService) {
        this.focoAedesService = focoAedesService;
    }

    @PostMapping("/registrar")
    @Operation(summary = "Registrar novo foco de Aedes",
            description = "Cria um novo registro de foco do mosquito Aedes aegypti no sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Foco registrado com sucesso",
                    content = @Content(schema = @Schema(implementation = FocoAedesResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<FocoAedesResponseDto> registrarFocoAedes(
            @Valid @RequestBody
            @Parameter(description = "Dados do foco de Aedes a ser registrado", required = true)
            FocoAedesCreateDto dto) {
        FocoAedesResponseDto novoFoco = focoAedesService.registrarFocoAedes(dto);
        return new ResponseEntity<>(novoFoco, HttpStatus.CREATED);
    }

    @GetMapping("/listar")
    @Operation(summary = "Listar focos de Aedes",
            description = "Retorna uma lista paginada de todos os focos de Aedes registrados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operação bem-sucedida"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<Page<FocoAedesResponseDto>> listarFocosAedes(
            @Parameter(description = "Parâmetros de paginação")
            Pageable pageable) {
        Page<FocoAedesResponseDto> focos = focoAedesService.listarFocosAedes(pageable);
        return ResponseEntity.ok(focos);
    }

    @GetMapping("/visita/{visitaId}")
    @Operation(summary = "Listar focos por visita",
            description = "Retorna todos os focos de Aedes associados a uma visita específica")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operação bem-sucedida"),
            @ApiResponse(responseCode = "404", description = "Visita não encontrada"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<List<FocoAedesResponseDto>> listarFocosPorVisita(
            @PathVariable
            @Parameter(description = "ID da visita", required = true)
            Long visitaId) {
        List<FocoAedesResponseDto> focos = focoAedesService.listarFocosPorVisita(visitaId);
        return ResponseEntity.ok(focos);
    }

    @GetMapping("/tipo/{tipoFoco}")
    @Operation(summary = "Listar focos por tipo",
            description = "Retorna todos os focos de Aedes de um tipo específico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operação bem-sucedida"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<List<FocoAedesResponseDto>> listarFocosPorTipo(
            @PathVariable
            @Parameter(description = "Tipo de foco (ex: CAIXA_DAGUA, PNEU, etc)", required = true)
            String tipoFoco) {
        List<FocoAedesResponseDto> focos = focoAedesService.listarFocosPorTipo(tipoFoco);
        return ResponseEntity.ok(focos);
    }

    @GetMapping("/tratado/{tratado}")
    @Operation(summary = "Listar focos por status de tratamento",
            description = "Retorna todos os focos de Aedes com base no status de tratamento")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operação bem-sucedida"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<List<FocoAedesResponseDto>> listarFocosPorStatusTratamento(
            @PathVariable
            @Parameter(description = "Status de tratamento (true para tratados, false para não tratados)", required = true)
            boolean tratado) {
        List<FocoAedesResponseDto> focos = focoAedesService.listarFocosPorStatusTratamento(tratado);
        return ResponseEntity.ok(focos);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar foco de Aedes por ID",
            description = "Retorna um foco de Aedes específico pelo seu ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operação bem-sucedida"),
            @ApiResponse(responseCode = "404", description = "Foco não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<FocoAedesResponseDto> buscarFocoAedesPorId(
            @PathVariable
            @Parameter(description = "ID do foco de Aedes", required = true)
            Long id) {
        FocoAedesResponseDto foco = focoAedesService.buscarFocoAedesPorId(id);
        return ResponseEntity.ok(foco);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar foco de Aedes",
            description = "Atualiza os dados de um foco de Aedes existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Foco atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos"),
            @ApiResponse(responseCode = "404", description = "Foco não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<FocoAedesResponseDto> atualizarFocoAedes(
            @PathVariable
            @Parameter(description = "ID do foco de Aedes", required = true)
            Long id,
            @Valid @RequestBody
            @Parameter(description = "Novos dados do foco de Aedes", required = true)
            FocoAedesCreateDto dto) {
        FocoAedesResponseDto focoAtualizado = focoAedesService.atualizarFocoAedes(id, dto);
        return ResponseEntity.ok(focoAtualizado);
    }

    @PatchMapping("/{id}/tratamento")
    @Operation(summary = "Atualizar tratamento de foco",
            description = "Atualiza as informações de tratamento de um foco de Aedes")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tratamento atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos"),
            @ApiResponse(responseCode = "404", description = "Foco não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<FocoAedesResponseDto> atualizarTratamento(
            @PathVariable
            @Parameter(description = "ID do foco de Aedes", required = true)
            Long id,
            @Valid @RequestBody
            @Parameter(description = "Dados do tratamento aplicado", required = true)
            FocoAedesTratamentoDto dto) {
        FocoAedesResponseDto focoAtualizado = focoAedesService.atualizarTratamento(id, dto);
        return ResponseEntity.ok(focoAtualizado);
    }

    @PatchMapping("/{id}/imagem")
    @Operation(summary = "Anexar imagem ao foco",
            description = "Adiciona ou atualiza a imagem associada a um foco de Aedes")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Imagem anexada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos"),
            @ApiResponse(responseCode = "404", description = "Foco não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<FocoAedesResponseDto> anexarImagem(
            @PathVariable
            @Parameter(description = "ID do foco de Aedes", required = true)
            Long id,
            @RequestParam("imagem")
            @Parameter(description = "Arquivo de imagem a ser anexado", required = true)
            MultipartFile imagem) {
        FocoAedesResponseDto focoAtualizado = focoAedesService.anexarImagem(id, imagem);
        return ResponseEntity.ok(focoAtualizado);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir foco de Aedes",
            description = "Remove um registro de foco de Aedes do sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Foco excluído com sucesso"),
            @ApiResponse(responseCode = "404", description = "Foco não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<Void> excluirFocoAedes(
            @PathVariable
            @Parameter(description = "ID do foco de Aedes", required = true)
            Long id) {
        focoAedesService.excluirFocoAedes(id);
        return ResponseEntity.noContent().build();
    }
}

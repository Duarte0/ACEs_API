package org.example.aces_api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.example.aces_api.dto.CasoDengueConfirmacaoLabDto;
import org.example.aces_api.dto.CasoDengueCreateDto;
import org.example.aces_api.dto.CasoDengueResponseDto;
import org.example.aces_api.dto.CasoDengueUpdateDto;
import org.example.aces_api.service.CasoDengueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/casos-dengue")
@Tag(name = "Casos de Dengue", description = "API para gerenciamento de casos de dengue")
public class CasoDengueController {

    private final CasoDengueService casoDengueService;

    @Autowired
    public CasoDengueController(CasoDengueService casoDengueService) {
        this.casoDengueService = casoDengueService;
    }

    @PostMapping("/registrar")
    @Operation(summary = "Registrar novo caso de dengue",
            description = "Cria um novo registro de caso de dengue no sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Caso de dengue registrado com sucesso",
                    content = @Content(schema = @Schema(implementation = CasoDengueResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<CasoDengueResponseDto> registrarCasoDengue(
            @Valid @RequestBody
            @Parameter(description = "Dados do caso de dengue a ser registrado", required = true)
            CasoDengueCreateDto dto) {
        CasoDengueResponseDto novoCaso = casoDengueService.registrarCasoDengue(dto);
        return new ResponseEntity<>(novoCaso, HttpStatus.CREATED);
    }

    @GetMapping("/listar")
    @Operation(summary = "Listar casos de dengue",
            description = "Retorna uma lista paginada de todos os casos de dengue")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operação bem-sucedida"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<Page<CasoDengueResponseDto>> listarCasosDengue(
            @Parameter(description = "Parâmetros de paginação")
            Pageable pageable) {
        Page<CasoDengueResponseDto> casos = casoDengueService.listarCasosDengue(pageable);
        return ResponseEntity.ok(casos);
    }

    @GetMapping("/paciente/{pacienteId}")
    @Operation(summary = "Listar casos de dengue por paciente",
            description = "Retorna todos os casos de dengue associados a um paciente específico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operação bem-sucedida"),
            @ApiResponse(responseCode = "404", description = "Paciente não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<List<CasoDengueResponseDto>> listarCasosPorPaciente(
            @PathVariable
            @Parameter(description = "ID do paciente", required = true)
            Long pacienteId) {
        List<CasoDengueResponseDto> casos = casoDengueService.listarCasosPorPaciente(pacienteId);
        return ResponseEntity.ok(casos);
    }

    @GetMapping("/visita/{visitaId}")
    @Operation(summary = "Listar casos de dengue por visita",
            description = "Retorna todos os casos de dengue associados a uma visita específica")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operação bem-sucedida"),
            @ApiResponse(responseCode = "404", description = "Visita não encontrada"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<List<CasoDengueResponseDto>> listarCasosPorVisita(
            @PathVariable
            @Parameter(description = "ID da visita", required = true)
            Long visitaId) {
        List<CasoDengueResponseDto> casos = casoDengueService.listarCasosPorVisita(visitaId);
        return ResponseEntity.ok(casos);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar caso de dengue por ID",
            description = "Retorna um caso de dengue específico pelo seu ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operação bem-sucedida"),
            @ApiResponse(responseCode = "404", description = "Caso de dengue não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<CasoDengueResponseDto> buscarCasoDenguePorId(
            @PathVariable
            @Parameter(description = "ID do caso de dengue", required = true)
            Long id) {
        CasoDengueResponseDto caso = casoDengueService.buscarCasoDenguePorId(id);
        return ResponseEntity.ok(caso);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar caso de dengue",
            description = "Atualiza os dados de um caso de dengue existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Caso de dengue atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos"),
            @ApiResponse(responseCode = "404", description = "Caso de dengue não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<CasoDengueResponseDto> atualizarCasoDengue(
            @PathVariable
            @Parameter(description = "ID do caso de dengue", required = true)
            Long id,
            @Valid @RequestBody
            @Parameter(description = "Novos dados do caso de dengue", required = true)
            CasoDengueUpdateDto dto) {
        CasoDengueResponseDto casoAtualizado = casoDengueService.atualizarCasoDengue(id, dto);
        return ResponseEntity.ok(casoAtualizado);
    }

    @PatchMapping("/{id}/confirmar-laboratorio")
    @Operation(summary = "Confirmar caso de dengue por laboratório",
            description = "Atualiza um caso de dengue com os resultados de confirmação laboratorial")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Confirmação laboratorial registrada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos"),
            @ApiResponse(responseCode = "404", description = "Caso de dengue não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<CasoDengueResponseDto> confirmarLaboratorio(
            @PathVariable
            @Parameter(description = "ID do caso de dengue", required = true)
            Long id,
            @Valid @RequestBody
            @Parameter(description = "Dados de confirmação laboratorial", required = true)
            CasoDengueConfirmacaoLabDto dto) {
        CasoDengueResponseDto casoAtualizado = casoDengueService.confirmarLaboratorio(id, dto);
        return ResponseEntity.ok(casoAtualizado);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir caso de dengue",
            description = "Remove um caso de dengue do sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Caso de dengue excluído com sucesso"),
            @ApiResponse(responseCode = "404", description = "Caso de dengue não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<Void> excluirCasoDengue(
            @PathVariable
            @Parameter(description = "ID do caso de dengue", required = true)
            Long id) {
        casoDengueService.excluirCasoDengue(id);
        return ResponseEntity.noContent().build();
    }
}

package org.example.aces_api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.example.aces_api.dto.VisitaCreateDto;
import org.example.aces_api.dto.VisitaResponseDto;
import org.example.aces_api.dto.VisitaUpdateStatusDto;
import org.example.aces_api.service.VisitaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.net.URI;

@RestController
@RequestMapping("/api/visitas")
@Tag(name = "Visitas", description = "API para gerenciamento de visitas de agentes de saúde")
public class VisitaController {

    private final VisitaService visitaService;

    @Autowired
    public VisitaController(VisitaService visitaService) {
        this.visitaService = visitaService;
    }

    @PostMapping("/registrar")
    @Operation(summary = "Registrar nova visita",
            description = "Cria um novo registro de visita de agente de saúde no sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Visita registrada com sucesso",
                    content = @Content(schema = @Schema(implementation = VisitaResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<VisitaResponseDto> registrarVisita(
            @Valid @RequestBody
            @Parameter(description = "Dados da visita a ser registrada", required = true)
            VisitaCreateDto visitaDto) {
        VisitaResponseDto visitaRegistrada = visitaService.registrarVisita(visitaDto);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(visitaRegistrada.id())
                .toUri();
        return ResponseEntity.created(location).body(visitaRegistrada);
    }

    @GetMapping("/listar")
    @Operation(summary = "Listar visitas",
            description = "Retorna uma lista paginada de todas as visitas registradas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operação bem-sucedida"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<Page<VisitaResponseDto>> listarVisitas(
            @Parameter(description = "Parâmetros de paginação")
            Pageable pageable) {
        Page<VisitaResponseDto> visitas = visitaService.listarVisitas(pageable);
        return ResponseEntity.ok(visitas);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar visita por ID",
            description = "Retorna uma visita específica pelo seu ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operação bem-sucedida"),
            @ApiResponse(responseCode = "404", description = "Visita não encontrada"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<VisitaResponseDto> buscarVisitaPorId(
            @PathVariable
            @Parameter(description = "ID da visita", required = true)
            Long id) {
        return visitaService.buscarVisitaPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Atualizar status da visita",
            description = "Atualiza o status de uma visita existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Status atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos"),
            @ApiResponse(responseCode = "404", description = "Visita não encontrada"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<VisitaResponseDto> atualizarStatus(
            @PathVariable
            @Parameter(description = "ID da visita", required = true)
            Long id,
            @Valid @RequestBody
            @Parameter(description = "Novo status da visita", required = true)
            VisitaUpdateStatusDto statusDto) {
        VisitaResponseDto visitaAtualizada = visitaService.atualizarStatus(id, statusDto.status());
        return ResponseEntity.ok(visitaAtualizada);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir visita",
            description = "Remove um registro de visita do sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Visita excluída com sucesso"),
            @ApiResponse(responseCode = "404", description = "Visita não encontrada"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<Void> excluirVisita(
            @PathVariable
            @Parameter(description = "ID da visita", required = true)
            Long id) {
        if (visitaService.excluirVisita(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}

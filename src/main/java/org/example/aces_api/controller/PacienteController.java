package org.example.aces_api.controller;

import jakarta.validation.Valid;
import org.example.aces_api.dto.PacienteCreateDto;
import org.example.aces_api.dto.PacienteResponseDto;
import org.example.aces_api.exception.EntityNotFoundException;
import org.example.aces_api.service.PacienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/pacientes")
@Tag(name = "Pacientes", description = "Endpoints para Gerenciar Pacientes")
public class PacienteController {

    @Autowired
    private PacienteService service;

    @GetMapping("/buscar/{id}")
    @Operation(summary = "Busca um Paciente por ID", description = "Busca um Paciente específico pelo seu ID",
            tags = {"Pacientes"},
            responses = {
                    @ApiResponse(description = "Sucesso", responseCode = "200",
                            content = @Content(schema = @Schema(implementation = PacienteResponseDto.class))),
                    @ApiResponse(description = "Nenhum Conteúdo", responseCode = "204", content = @Content),
                    @ApiResponse(description = "Requisição Inválida", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Não Autorizado", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Não Encontrado", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Erro Interno", responseCode = "500", content = @Content)
            }
    )
    public ResponseEntity<EntityModel<PacienteResponseDto>> getPacienteById(@PathVariable Integer id) {
        try {
            EntityModel<PacienteResponseDto> paciente = service.findById(id);
            return ResponseEntity.ok(paciente);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping("/cadastrar")
    @Operation(summary = "Adiciona um novo Paciente",
            description = "Adiciona um novo Paciente passando uma representação JSON do paciente!",
            tags = {"Pacientes"},
            responses = {
                    @ApiResponse(description = "Criado", responseCode = "201",
                            content = @Content(schema = @Schema(implementation = PacienteResponseDto.class))),
                    @ApiResponse(description = "Requisição Inválida", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Não Autorizado", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Erro Interno", responseCode = "500", content = @Content)
            }
    )
    public ResponseEntity<EntityModel<PacienteResponseDto>> createPaciente(@Valid @RequestBody PacienteCreateDto pacienteCreateDto) {
        EntityModel<PacienteResponseDto> createdPaciente = service.criarPaciente(pacienteCreateDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPaciente);
    }

    @PutMapping("/atualizar/{id}")
    @Operation(summary = "Atualiza um Paciente",
            description = "Atualiza um Paciente existente passando uma representação JSON do paciente!",
            tags = {"Pacientes"},
            responses = {
                    @ApiResponse(description = "Atualizado", responseCode = "200",
                            content = @Content(schema = @Schema(implementation = PacienteResponseDto.class))),
                    @ApiResponse(description = "Requisição Inválida", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Não Autorizado", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Não Encontrado", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Erro Interno", responseCode = "500", content = @Content)
            }
    )
    public ResponseEntity<EntityModel<PacienteResponseDto>> updatePaciente(@PathVariable Integer id, @Valid @RequestBody PacienteCreateDto pacienteCreateDto) {
        try {
            EntityModel<PacienteResponseDto> updatedPaciente = service.atualizarPaciente(id, pacienteCreateDto);
            return ResponseEntity.ok(updatedPaciente);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/buscar/todos")
    @Operation(summary = "Busca todos os Pacientes", description = "Busca todos os Pacientes",
            tags = {"Pacientes"},
            responses = {
                    @ApiResponse(description = "Sucesso", responseCode = "200",
                            content = @Content(mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = PacienteResponseDto.class)))),
                    @ApiResponse(description = "Nenhum Conteúdo", responseCode = "204", content = @Content),
                    @ApiResponse(description = "Requisição Inválida", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Não Autorizado", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Erro Interno", responseCode = "500", content = @Content)
            }
    )
    public ResponseEntity<CollectionModel<EntityModel<PacienteResponseDto>>> getAllPacientes() {
        CollectionModel<EntityModel<PacienteResponseDto>> pacientes = service.findAll();
        if (pacientes.getContent().isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(pacientes);
    }

    @DeleteMapping("/apagar/{id}")
    @Operation(summary = "Exclui um Paciente",
            description = "Exclui um Paciente pelo ID",
            tags = {"Pacientes"}, //
            responses = { //
                    @ApiResponse(description = "Nenhum Conteúdo", responseCode = "204", content = @Content),
                    @ApiResponse(description = "Requisição Inválida", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Não Autorizado", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Não Encontrado", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Erro Interno", responseCode = "500", content = @Content)
            }
    )
    public ResponseEntity<Void> deletePaciente(@PathVariable Integer id) {
        try {
            service.excluirPaciente(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
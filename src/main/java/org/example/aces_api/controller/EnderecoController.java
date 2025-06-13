package org.example.aces_api.controller;

import jakarta.validation.Valid;
import org.example.aces_api.dto.EnderecoCreateDto;
import org.example.aces_api.dto.EnderecoResponseDto;
import org.example.aces_api.exception.EntityNotFoundException;
import org.example.aces_api.service.EnderecoService;

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
@RequestMapping("/api/enderecos")
@Tag(name = "Endereços", description = "Endpoints para Gerenciar Endereços")
public class EnderecoController {

    @Autowired
    private EnderecoService service;

    @GetMapping("/buscar/{id}")
    @Operation(summary = "Busca um Endereço por ID", description = "Busca um Endereço específico pelo seu ID",
            tags = {"Endereços"},
            responses = {
                    @ApiResponse(description = "Sucesso", responseCode = "200",
                            content = @Content(schema = @Schema(implementation = EnderecoResponseDto.class))),
                    @ApiResponse(description = "Nenhum Conteúdo", responseCode = "204", content = @Content),
                    @ApiResponse(description = "Requisição Inválida", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Não Autorizado", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Não Encontrado", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Erro Interno", responseCode = "500", content = @Content)
            }
    )
    public ResponseEntity<EntityModel<EnderecoResponseDto>> getEnderecoById(@PathVariable Integer id) {
        try {
            EntityModel<EnderecoResponseDto> endereco = service.findById(id);
            return ResponseEntity.ok(endereco);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping("/cadastrar")
    @Operation(summary = "Adiciona um novo Endereço",
            description = "Adiciona um novo Endereço passando uma representação JSON do endereço!",
            tags = {"Endereços"},
            responses = {
                    @ApiResponse(description = "Criado", responseCode = "201",
                            content = @Content(schema = @Schema(implementation = EnderecoResponseDto.class))),
                    @ApiResponse(description = "Requisição Inválida", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Não Autorizado", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Erro Interno", responseCode = "500", content = @Content)
            }
    )
    public ResponseEntity<EntityModel<EnderecoResponseDto>> createEndereco(@Valid @RequestBody EnderecoCreateDto enderecoCreateDto) {
        EntityModel<EnderecoResponseDto> createdEndereco = service.criarEndereco(enderecoCreateDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdEndereco);
    }

    @PutMapping("/atualizar/{id}")
    @Operation(summary = "Atualiza um Endereço",
            description = "Atualiza um Endereço existente passando uma representação JSON do endereço!",
            tags = {"Endereços"},
            responses = {
                    @ApiResponse(description = "Atualizado", responseCode = "200",
                            content = @Content(schema = @Schema(implementation = EnderecoResponseDto.class))),
                    @ApiResponse(description = "Requisição Inválida", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Não Autorizado", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Não Encontrado", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Erro Interno", responseCode = "500", content = @Content)
            }
    )
    public ResponseEntity<EntityModel<EnderecoResponseDto>> updateEndereco(@PathVariable Integer id, @Valid @RequestBody EnderecoCreateDto enderecoCreateDto) {
        try {
            EntityModel<EnderecoResponseDto> updatedEndereco = service.atualizarEndereco(id, enderecoCreateDto);
            return ResponseEntity.ok(updatedEndereco);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/buscar/todos")
    @Operation(summary = "Busca todos os Endereços", description = "Busca todos os Endereços",
            tags = {"Endereços"},
            responses = {
                    @ApiResponse(description = "Sucesso", responseCode = "200",
                            content = @Content(mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = EnderecoResponseDto.class)))),
                    @ApiResponse(description = "Nenhum Conteúdo", responseCode = "204", content = @Content),
                    @ApiResponse(description = "Requisição Inválida", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Não Autorizado", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Erro Interno", responseCode = "500", content = @Content)
            }
    )
    public ResponseEntity<CollectionModel<EntityModel<EnderecoResponseDto>>> getAllEnderecos() {
        CollectionModel<EntityModel<EnderecoResponseDto>> enderecos = service.findAll();
        if (enderecos.getContent().isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(enderecos);
    }

    @DeleteMapping("/apagar/{id}")
    @Operation(summary = "Exclui um Endereço",
            description = "Exclui um Endereço pelo ID",
            tags = {"Endereços"},
            responses = {
                    @ApiResponse(description = "Nenhum Conteúdo", responseCode = "204", content = @Content),
                    @ApiResponse(description = "Requisição Inválida", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Não Autorizado", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Não Encontrado", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Erro Interno", responseCode = "500", content = @Content)
            }
    )
    public ResponseEntity<Void> deleteEndereco(@PathVariable Integer id) {
        try {
            service.excluirEndereco(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
package org.example.aces_api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.example.aces_api.dto.*;
import org.example.aces_api.exception.EntityNotFoundException;
import org.example.aces_api.exception.ErrorMessage;
import org.example.aces_api.model.entity.Area;
import org.example.aces_api.service.AgenteService;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
@Tag(name = "Agentes", description = "Contem todas relações relativas ao recurso Agente")
@RestController
@RequestMapping("/api/agentes")
public class AgenteController {

    private final AgenteService agenteService;

    public AgenteController(AgenteService agenteService) {
        this.agenteService = agenteService;
    }


    @Operation(
            summary = "Criar um novo agente",
            description = "Registra um novo agente no sistema com os dados fornecidos"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Agente criado com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AgenteResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorMessage.class))),
            @ApiResponse(responseCode = "409", description = "Conflito - Matrícula já cadastrada",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorMessage.class)))
    })
    @PostMapping
    public ResponseEntity<EntityModel<AgenteResponseDTO>> criarAgente(@Valid @RequestBody AgenteRequestDTO agenteRequestDTO) {
        AgenteResponseDTO agenteCriado = agenteService.criarAgente(agenteRequestDTO);

        EntityModel<AgenteResponseDTO> model = EntityModel.of(agenteCriado);
        model.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(AgenteController.class).buscarPorId(agenteCriado.id())).withSelfRel());
        model.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(AgenteController.class).listarAreasDoAgente(agenteCriado.id())).withRel("areas"));

        return ResponseEntity.status(HttpStatus.CREATED).body(model);
    }

    @Operation(
            summary = "Obter agente por ID",
            description = "Recupera os detalhes de um agente específico pelo seu ID"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Agente encontrado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AgenteResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Agente não encontrado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorMessage.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<AgenteResponseDTO>> buscarPorId(@PathVariable Integer id) {
        try {
            AgenteResponseDTO agente = agenteService.buscarPorId(id);

            EntityModel<AgenteResponseDTO> model = EntityModel.of(agente);
            model.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(AgenteController.class).buscarPorId(id)).withSelfRel());
            model.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(AgenteController.class).listarTodos(true)).withRel("todos-agentes"));
            model.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(AgenteController.class).listarAreasDoAgente(id)).withRel("areas"));

            return ResponseEntity.ok(model);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(
            summary = "Listar todos os agentes",
            description = "Recupera uma lista de todos os agentes, opcionalmente filtrados por status ativo/inativo"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de agentes recuperada com sucesso",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "204", description = "Nenhum agente encontrado")
    })
    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<AgenteResponseDTO>>> listarTodos(
            @RequestParam(required = false) Boolean ativo) {

        List<AgenteResponseDTO> agentes = agenteService.listarTodos();

        List<EntityModel<AgenteResponseDTO>> agentesComLinks = agentes.stream()
                .map(agente -> {
                    EntityModel<AgenteResponseDTO> model = EntityModel.of(agente);
                    model.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(AgenteController.class).buscarPorId(agente.id())).withSelfRel());
                    return model;
                })
                .collect(Collectors.toList());

        CollectionModel<EntityModel<AgenteResponseDTO>> collectionModel = CollectionModel.of(agentesComLinks);
        collectionModel.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(AgenteController.class).listarTodos(true)).withSelfRel());

        return agentes.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(collectionModel);
    }


    @Operation(
            summary = "Atualizar agente",
            description = "Atualiza os dados de um agente existente"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Agente atualizado com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AgenteResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorMessage.class))),
            @ApiResponse(responseCode = "404", description = "Agente não encontrado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorMessage.class))),
            @ApiResponse(responseCode = "409", description = "Conflito - Nova matrícula já em uso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorMessage.class)))
    })

    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<AgenteResponseDTO>> atualizarAgente(
            @PathVariable Integer id,
            @Valid @RequestBody AgenteUpdateDTO agenteUpdateDTO) {
        try {
            AgenteResponseDTO agenteAtualizado = agenteService.atualizarAgente(id, agenteUpdateDTO);

            EntityModel<AgenteResponseDTO> model = EntityModel.of(agenteAtualizado);
            model.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(AgenteController.class).buscarPorId(id)).withSelfRel());
            model.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(AgenteController.class).listarAreasDoAgente(id)).withRel("areas"));

            return ResponseEntity.ok(model);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(
            summary = "Ativar agente",
            description = "Altera o status de um agente para ativo"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Agente ativado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Agente não encontrado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorMessage.class)))
    })
    @PatchMapping("/{id}/ativar")
    public ResponseEntity<Void> ativarAgente(@PathVariable Integer id) {
        try {
            agenteService.ativarAgente(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(
            summary = "Desativar agente",
            description = "Altera o status de um agente para inativo"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Agente desativado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Agente não encontrado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorMessage.class)))
    })
    @PatchMapping("/{id}/desativar")
    public ResponseEntity<Void> desativarAgente(@PathVariable Integer id) {
        try {
            agenteService.desativarAgente(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(
            summary = "Buscar agente por Matricula",
            description = "Realiza uma busca de um agente específico pela matrícula"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Agente econtrado com sucesso.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = RelatorioDTO.class))),
            @ApiResponse(responseCode = "400", description = "Requisição inválida, verifique os parâmetros",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
            @ApiResponse(responseCode = "404", description = "Agente com Matrícula específicado não encontrado.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
    })
    @GetMapping("/matricula/{matricula}")
    public ResponseEntity<EntityModel<AgenteResponseDTO>> buscarPorMatricula(@PathVariable String matricula) {
        try {
            AgenteResponseDTO agente = agenteService.buscarPorMatricula(matricula);

            EntityModel<AgenteResponseDTO> model = EntityModel.of(agente);
            model.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(AgenteController.class).buscarPorMatricula(matricula)).withSelfRel());
            model.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(AgenteController.class).listarAreasDoAgente(agente.id())).withRel("areas"));

            return ResponseEntity.ok(model);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(
            summary = "Buscar agentes por nome",
            description = "Recupera uma lista de agentes cujo nome contém o texto fornecido (case insensitive)"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Agentes encontrados",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "204", description = "Nenhum agente encontrado")
    })
    @GetMapping("/buscar")

    public ResponseEntity<CollectionModel<EntityModel<AgenteResponseDTO>>> buscarPorNome(
            @RequestParam String nome) {
        List<AgenteResponseDTO> agentes = agenteService.buscarPorNome(nome);

        List<EntityModel<AgenteResponseDTO>> agentesComLinks = agentes.stream()
                .map(agente -> {
                    EntityModel<AgenteResponseDTO> model = EntityModel.of(agente);
                    model.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(AgenteController.class).buscarPorId(agente.id())).withSelfRel());
                    return model;
                })
                .collect(Collectors.toList());

        CollectionModel<EntityModel<AgenteResponseDTO>> collectionModel = CollectionModel.of(agentesComLinks);
        collectionModel.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(AgenteController.class).buscarPorNome(nome)).withSelfRel());

        return agentes.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(collectionModel);
    }


    @Operation(
            summary = "Listar áreas do agente",
            description = "Recupera a lista de áreas associadas a um agente específico"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Áreas encontradas",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Agente não encontrado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorMessage.class)))
    })
    @GetMapping("/{agenteId}/areas")
    public ResponseEntity<List<Area>> listarAreasDoAgente(
            @PathVariable Integer agenteId) {
        List<Area> areas = agenteService.listarAreasPorAgente(agenteId);
        return ResponseEntity.ok(areas);
    }

    @Operation(
            summary = "Associar área ao agente",
            description = "Cria uma associação entre um agente e uma área de atuação"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Área associada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorMessage.class))),
            @ApiResponse(responseCode = "404", description = "Agente ou área não encontrado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorMessage.class))),
            @ApiResponse(responseCode = "409", description = "Associação já existe",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorMessage.class)))
    })
    @PostMapping("/associar-area")
    public ResponseEntity<String> associarAreaAoAgente(
            @Valid @RequestBody AssociarAreaAgenteDto dto) {
        agenteService.associarArea(dto.agenteId(), dto.areaId());
        return ResponseEntity.ok("Área associada ao agente com sucesso");
    }

    @Operation(
            summary = "Remover área do agente",
            description = "Remove uma associação existente entre um agente e uma área de atuação"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Área removida com sucesso"),
            @ApiResponse(responseCode = "404", description = "Associação não encontrada",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorMessage.class)))
    })
    @DeleteMapping("/remover-area")
    public ResponseEntity<String> removerAreaDoAgente(
            @Valid @RequestBody AssociarAreaAgenteDto dto) {
        agenteService.removerArea(dto.agenteId(), dto.areaId());
        return ResponseEntity.ok("Área removida do agente com sucesso");
    }
}
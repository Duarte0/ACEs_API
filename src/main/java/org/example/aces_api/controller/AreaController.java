package org.example.aces_api.controller;

import jakarta.validation.Valid;
import org.example.aces_api.dto.AreaCreateDto;
import org.example.aces_api.dto.AreaResponseDto;
import org.example.aces_api.exception.EntityNotFoundException;
import org.example.aces_api.service.AreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.CollectionModel;

import io.swagger.v3.oas.annotations.Operation; // Importar Operation
import io.swagger.v3.oas.annotations.media.ArraySchema; // Importar ArraySchema
import io.swagger.v3.oas.annotations.media.Content; // Importar Content
import io.swagger.v3.oas.annotations.media.Schema; // Importar Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse; // Importar ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag; // Importar Tag

import java.util.List;

@RestController
@RequestMapping("/api/areas")
@Tag(name = "Areas", description = "Endpoints for Managing Areas") // Anotação para descrever o controlador
public class AreaController {

    @Autowired
    private AreaService areaService;

    @GetMapping("/buscar/{id}")
    @Operation(summary = "Finds an Area by ID", description = "Finds an Area by ID",
            tags = {"Areas"}, // Associa a tag "Areas" a este endpoint
            responses = { // Define as possíveis respostas para esta operação
                    @ApiResponse(description = "Success", responseCode = "200",
                            content = @Content(schema = @Schema(implementation = AreaResponseDto.class))), // Retorna um objeto AreaResponseDto
                    @ApiResponse(description = "No Content", responseCode = "204", content = @Content), //
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content), //
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content), //
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content), //
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content) //
            }
    )
    public ResponseEntity<EntityModel<AreaResponseDto>> getAreaById(@PathVariable Integer id) {
        try {
            EntityModel<AreaResponseDto> area = areaService.findById(id);
            return ResponseEntity.ok(area);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping("/cadastrar")
    @Operation(summary = "Adds a new Area",
            description = "Adds a new Area by passing in a JSON representation of the area!",
            tags = {"Areas"}, // Associa a tag "Areas" a este endpoint
            responses = {
                    @ApiResponse(description = "Created", responseCode = "201",
                            content = @Content(schema = @Schema(implementation = AreaResponseDto.class))), // Retorna um objeto AreaResponseDto
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content), //
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content), //
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content) //
            }
    )
    public ResponseEntity<EntityModel<AreaResponseDto>> createArea(@Valid @RequestBody AreaCreateDto areaCreateDto) {
        EntityModel<AreaResponseDto> createdArea = areaService.criarArea(areaCreateDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdArea);
    }

    @PutMapping("/atualizar/{id}")
    @Operation(summary = "Updates an Area",
            description = "Updates an Area by passing in a JSON representation of the area!",
            tags = {"Areas"}, // Associa a tag "Areas" a este endpoint
            responses = {
                    @ApiResponse(description = "Updated", responseCode = "200",
                            content = @Content(schema = @Schema(implementation = AreaResponseDto.class))), // Retorna um objeto AreaResponseDto
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content), //
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content), //
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content), //
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content) //
            }
    )
    public ResponseEntity<EntityModel<AreaResponseDto>> updateArea(@PathVariable Integer id, @Valid @RequestBody AreaCreateDto areaCreateDto) {
        try {
            EntityModel<AreaResponseDto> updatedArea = areaService.atualizarArea(id, areaCreateDto);
            return ResponseEntity.ok(updatedArea);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/buscar/todos")
    @Operation(summary = "Finds all Areas", description = "Finds all Areas",
            tags = {"Areas"}, // Associa a tag "Areas" a este endpoint
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200",
                            content = @Content(mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = AreaResponseDto.class)))), // Retorna um array de AreaResponseDto
                    @ApiResponse(description = "No Content", responseCode = "204", content = @Content),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content), //
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content), //
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content) //
            }
    )
    public ResponseEntity<CollectionModel<EntityModel<AreaResponseDto>>> getAllAreas() {
        CollectionModel<EntityModel<AreaResponseDto>> areas = areaService.findAll();

        if (areas.getContent().isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(areas);
    }

    @DeleteMapping("/apagar/{id}")
    @Operation(summary = "Deletes an Area",
            description = "Deletes an Area by ID",
            tags = {"Areas"}, // Associa a tag "Areas" a este endpoint
            responses = {
                    @ApiResponse(description = "No Content", responseCode = "204", content = @Content), // Como não tem retorno, usa @Content padrão
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content), //
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content), //
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content), //
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content) //
            }
    )
    public ResponseEntity<Void> deleteArea(@PathVariable Integer id) {
        try {
            areaService.excluirArea(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
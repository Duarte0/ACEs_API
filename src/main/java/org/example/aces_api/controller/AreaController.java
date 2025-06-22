package org.example.aces_api.controller;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.example.aces_api.dto.AreaCreateDto;
import org.example.aces_api.dto.AreaResponseDto;
import org.example.aces_api.dto.RelatorioDTO;
import org.example.aces_api.exception.EntityNotFoundException;
import org.example.aces_api.exception.ErrorMessage;
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

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

@RestController
@RequestMapping("/api/areas")
@Tag(name = "Areas", description = "Endpoits para gerenciar Areas") // Anotação para descrever o controlador
public class AreaController {

    @Autowired
    private AreaService areaService;


    public AreaController(AreaService areaService) {
        this.areaService = areaService;
    }

    @GetMapping("/buscar/{id}")
    @Operation(summary = "Busca uma area por ID", description = "Busca uma area por ID",
            tags = {"Areas"},
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200",
                            content = @Content(schema = @Schema(implementation = AreaResponseDto.class))),
                    @ApiResponse(description = "No Content", responseCode = "204", content = @Content),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)
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
    @Operation(summary = "Adiciona uma nova area",
            description = "Adiciona uma nova area",
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
    @Operation(summary = "Atualiza uma area",
            description = "Atualiza uma area",
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
    @Operation(summary = "Busca todas as Areas", description = "Busca todas as Areas",
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
    @Operation(summary = "Deleta uma Area",
            description = "Deleta uma area pelo ID",
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

    @Operation(
            summary = "Gera um relatório detalhado para uma área específica",
            description = "Calcula e retorna um relatório com totais de visitas, casos de dengue, e focos de Aedes para uma área específica dentro de um período de tempo. Se o período não for fornecido, considera os últimos 30 dias por padrão. A resposta inclui links HATEOAS para navegação."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Relatório gerado com sucesso.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = RelatorioDTO.class))),
            @ApiResponse(responseCode = "400", description = "Requisição inválida. Verifique os parâmetros, como o formato da data (deve ser yyyy-MM-dd).",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
            @ApiResponse(responseCode = "404", description = "A área com o ID especificado não foi encontrada.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
    })
    @GetMapping("/{id}/relatorio")
    public ResponseEntity<EntityModel<RelatorioDTO>> getRelatorioDaArea(
            @Parameter(description = "ID da área para a qual o relatório será gerado", required = true, example = "101")
            @PathVariable Long id,
            @Parameter(description = "Data de início do período (formato: yyyy-MM-dd)", example = "2025-05-01")
            @RequestParam(name = "dataInicio", required = false) String dataInicioStr,
            @Parameter(description = "Data de fim do período (formato: yyyy-MM-dd)", example = "2025-05-31")
            @RequestParam(name = "dataFim", required = false) String dataFimStr) {
        try {
            LocalDate dataFim = (dataFimStr != null) ? LocalDate.parse(dataFimStr) : LocalDate.now();
            LocalDate dataInicio = (dataInicioStr != null) ? LocalDate.parse(dataInicioStr) : dataFim.minusDays(30);

            EntityModel<RelatorioDTO> relatorioModel = areaService.gerarRelatorio(id, dataInicio, dataFim);

            return ResponseEntity.ok(relatorioModel);

        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Formato de data inválido. Use o formato yyyy-MM-dd.", e);
        }
    }
}
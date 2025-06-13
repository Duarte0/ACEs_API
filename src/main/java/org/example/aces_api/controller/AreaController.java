package org.example.aces_api.controller;

import jakarta.validation.Valid;
import org.example.aces_api.dto.AreaCreateDto;
import org.example.aces_api.dto.FullReportDto;
import org.example.aces_api.dto.AreaResponseDto;
import org.example.aces_api.dto.RelatorioDTO;
import org.example.aces_api.exception.EntityNotFoundException;
import org.example.aces_api.service.AreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/areas")
public class AreaController {

    @Autowired
    private AreaService areaService;

    public AreaController(AreaService areaService) {
        this.areaService = areaService;
    }


    @GetMapping("/buscar/{id}")
    public ResponseEntity<AreaResponseDto> getAreaById(@PathVariable Integer id) {
        try {
            AreaResponseDto area = areaService.findById(id);
            return ResponseEntity.ok(area);
        } catch (EntityNotFoundException e) {

            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }


    @PostMapping("/cadastrar")
    public ResponseEntity<AreaResponseDto> createArea(@Valid @RequestBody AreaCreateDto areaCreateDto) {

        AreaResponseDto createdArea = areaService.criarArea(areaCreateDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(createdArea);
    }

    @PutMapping("/atualizar/{id}")
    public ResponseEntity<AreaResponseDto> updateArea(@PathVariable Integer id, @Valid @RequestBody AreaCreateDto areaCreateDto) {
        try {
            AreaResponseDto updatedArea = areaService.atualizarArea(id, areaCreateDto);
            return ResponseEntity.ok(updatedArea);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/buscar/todos")
    public ResponseEntity<List<AreaResponseDto>> getAllAreas() {
        List<AreaResponseDto> areas = areaService.findAll();

        if (areas.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(areas);
    }


    @DeleteMapping("/apagar/{id}")
    public ResponseEntity<Void> deleteArea(@PathVariable Integer id) {
        try {
            areaService.excluirArea(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/{id}/relatorio")
    public ResponseEntity<RelatorioDTO> getRelatorioDaArea(@PathVariable Long id,
                                                           @RequestParam(name = "dataInicio", required = false) String dataInicioStr,
                                                           @RequestParam(name = "dataFim", required = false) String dataFimStr) {

        LocalDate dataFim = (dataFimStr == null) ? LocalDate.now() : LocalDate.parse(dataFimStr);
        LocalDate dataInicio = (dataInicioStr == null) ? LocalDate.now() : LocalDate.parse(dataInicioStr);

        RelatorioDTO relatorio = areaService.gerarRelatorio(id, dataInicio, dataFim);

        return ResponseEntity.ok(relatorio);

    }

}
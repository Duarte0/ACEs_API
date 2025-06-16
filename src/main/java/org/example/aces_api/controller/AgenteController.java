package org.example.aces_api.controller;

import jakarta.validation.Valid;
import org.example.aces_api.dto.AgenteRequestDTO;
import org.example.aces_api.dto.AgenteResponseDTO;
import org.example.aces_api.dto.AgenteUpdateDTO;
import org.example.aces_api.dto.AssociarAreaAgenteDto;
import org.example.aces_api.exception.EntityNotFoundException;
import org.example.aces_api.model.entity.Area;
import org.example.aces_api.service.AgenteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/agentes")
public class AgenteController {

    private final AgenteService agenteService;

    public AgenteController(AgenteService agenteService) {
        this.agenteService = agenteService;
    }

    @PostMapping
    public ResponseEntity<AgenteResponseDTO> criarAgente(@Valid @RequestBody AgenteRequestDTO agenteRequestDTO) {
        AgenteResponseDTO agenteCriado = agenteService.criarAgente(agenteRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(agenteCriado);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AgenteResponseDTO> buscarPorId(@PathVariable Integer id) {
        try {
            AgenteResponseDTO agente = agenteService.buscarPorId(id);
            return ResponseEntity.ok(agente);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<AgenteResponseDTO>> listarTodos(
            @RequestParam(required = false) Boolean ativo) {

        List<AgenteResponseDTO> agentes = agenteService.listarTodos();

        return agentes.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(agentes);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AgenteResponseDTO> atualizarAgente(
            @PathVariable Integer id,
            @Valid @RequestBody AgenteUpdateDTO agenteUpdateDTO) {
        try {
            AgenteResponseDTO agenteAtualizado = agenteService.atualizarAgente(id, agenteUpdateDTO);
            return ResponseEntity.ok(agenteAtualizado);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{id}/ativar")
    public ResponseEntity<Void> ativarAgente(@PathVariable Integer id) {
        try {
            agenteService.ativarAgente(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{id}/desativar")
    public ResponseEntity<Void> desativarAgente(@PathVariable Integer id) {
        try {
            agenteService.desativarAgente(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/matricula/{matricula}")
    public ResponseEntity<AgenteResponseDTO> buscarPorMatricula(@PathVariable String matricula) {
        try {
            AgenteResponseDTO agente = agenteService.buscarPorMatricula(matricula);
            return ResponseEntity.ok(agente);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<AgenteResponseDTO>> buscarPorNome(
            @RequestParam String nome) {
        List<AgenteResponseDTO> agentes = agenteService.buscarPorNome(nome);
        return agentes.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(agentes);
    }

        @GetMapping("/{agenteId}/areas")
    public ResponseEntity<List<Area>> listarAreasDoAgente(
            @PathVariable Integer agenteId) {
        List<Area> areas = agenteService.listarAreasPorAgente(agenteId);
        return ResponseEntity.ok(areas);
    }

    @PostMapping("/associar-area")
    public ResponseEntity<String> associarAreaAoAgente(
            @Valid @RequestBody AssociarAreaAgenteDto dto) {
        agenteService.associarArea(dto.agenteId(), dto.areaId());
        return ResponseEntity.ok("Área associada ao agente com sucesso");
    }

    @DeleteMapping("/remover-area")
    public ResponseEntity<String> removerAreaDoAgente(
            @Valid @RequestBody AssociarAreaAgenteDto dto) {
        agenteService.removerArea(dto.agenteId(), dto.areaId());
        return ResponseEntity.ok("Área removida do agente com sucesso");
    }
}
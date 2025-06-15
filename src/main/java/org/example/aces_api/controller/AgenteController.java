package org.example.aces_api.controller;

import jakarta.validation.Valid;
import org.example.aces_api.dto.AgenteRequestDTO;
import org.example.aces_api.dto.AgenteResponseDTO;
import org.example.aces_api.dto.AgenteUpdateDTO;
import org.example.aces_api.exception.EntityNotFoundException;
import org.example.aces_api.service.AgenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/agentes")
public class AgenteController {

    @Autowired
    private AgenteService agenteService;

    @GetMapping("/buscar/{id}")
    public ResponseEntity<AgenteResponseDTO> buscarPorId(@PathVariable Integer id) {
        try {
            AgenteResponseDTO agente = agenteService.buscarPorId(id);
            return ResponseEntity.ok(agente);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping("/cadastrar")
    public ResponseEntity<AgenteResponseDTO> cadastrarAgente(@Valid @RequestBody AgenteRequestDTO agenteRequestDTO) {
        AgenteResponseDTO agenteCriado = agenteService.criarAgente(agenteRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(agenteCriado);
    }

    @PutMapping("/atualizar/{id}")
    public ResponseEntity<AgenteResponseDTO> atualizarAgente(
            @PathVariable Integer id,
            @Valid @RequestBody AgenteUpdateDTO agenteUpdateDTO) {
        try {
            AgenteResponseDTO agenteAtualizado = agenteService.atualizarAgente(id, agenteUpdateDTO);
            return ResponseEntity.ok(agenteAtualizado);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/buscar/todos")
    public ResponseEntity<List<AgenteResponseDTO>> listarTodos() {
        List<AgenteResponseDTO> agentes = agenteService.listarTodos();

        if (agentes.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(agentes);
    }

    @PatchMapping("/desativar/{id}")
    public ResponseEntity<Void> desativarAgente(@PathVariable Integer id) {
        try {
            agenteService.desativarAgente(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PatchMapping("/ativar/{id}")
    public ResponseEntity<Void> ativarAgente(@PathVariable Integer id) {
        try {
            agenteService.ativarAgente(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
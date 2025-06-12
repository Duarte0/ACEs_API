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
import org.springframework.hateoas.EntityModel; // Importar EntityModel
import org.springframework.hateoas.CollectionModel; // Importar CollectionModel

import java.util.List;

@RestController
@RequestMapping("/api/areas")
public class AreaController {

    @Autowired
    private AreaService areaService;


    @GetMapping("/buscar/{id}")
    // O tipo de retorno muda para ResponseEntity<EntityModel<AreaResponseDto>>
    public ResponseEntity<EntityModel<AreaResponseDto>> getAreaById(@PathVariable Integer id) {
        try {
            EntityModel<AreaResponseDto> area = areaService.findById(id);
            return ResponseEntity.ok(area);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }


    @PostMapping("/cadastrar")
    // O tipo de retorno muda para ResponseEntity<EntityModel<AreaResponseDto>>
    public ResponseEntity<EntityModel<AreaResponseDto>> createArea(@Valid @RequestBody AreaCreateDto areaCreateDto) {
        EntityModel<AreaResponseDto> createdArea = areaService.criarArea(areaCreateDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdArea);
    }

    @PutMapping("/atualizar/{id}")
    // O tipo de retorno muda para ResponseEntity<EntityModel<AreaResponseDto>>
    public ResponseEntity<EntityModel<AreaResponseDto>> updateArea(@PathVariable Integer id, @Valid @RequestBody AreaCreateDto areaCreateDto) {
        try {
            EntityModel<AreaResponseDto> updatedArea = areaService.atualizarArea(id, areaCreateDto);
            return ResponseEntity.ok(updatedArea);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/buscar/todos")
    // O tipo de retorno muda para ResponseEntity<CollectionModel<EntityModel<AreaResponseDto>>>
    public ResponseEntity<CollectionModel<EntityModel<AreaResponseDto>>> getAllAreas() {
        CollectionModel<EntityModel<AreaResponseDto>> areas = areaService.findAll();

        // O CollectionModel pode ser vazio, mas o .isEmpty() aqui precisaria ser adaptado
        // para verificar se a lista de "content" dentro do CollectionModel está vazia.
        // Para simplificar, vou manter a verificação que retorna noContent se não houver conteúdo.
        if (areas.getContent().isEmpty()) { // Verifica se o conteúdo do CollectionModel está vazio
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

}
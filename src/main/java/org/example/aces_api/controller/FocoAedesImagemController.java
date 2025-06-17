package org.example.aces_api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.aces_api.service.ArmazenamentoImagemService;
import org.example.aces_api.service.FocoAedesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/focos-aedes")
@Tag(name = "Imagens de Focos de Aedes", description = "API para gerenciamento de imagens de focos do mosquito Aedes aegypti")
public class FocoAedesImagemController {

    private final FocoAedesService focoAedesService;
    private final ArmazenamentoImagemService armazenamentoImagemService;

    @Autowired
    public FocoAedesImagemController(FocoAedesService focoAedesService,
                                     ArmazenamentoImagemService armazenamentoImagemService) {
        this.focoAedesService = focoAedesService;
        this.armazenamentoImagemService = armazenamentoImagemService;
    }

    @GetMapping("/{id}/imagem")
    @Operation(summary = "Obter imagem de um foco",
            description = "Recupera a imagem associada a um foco de Aedes específico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Imagem recuperada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Foco não encontrado ou sem imagem"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<Map<String, String>> getImagem(
            @PathVariable
            @Parameter(description = "ID do foco de Aedes", required = true)
            Long id) {

        String imagemBase64 = focoAedesService.obterImagemFoco(id);

        if (imagemBase64 == null || imagemBase64.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Map<String, String> response = new HashMap<>();
        response.put("imagem", imagemBase64);

        return ResponseEntity.ok(response);
    }
    @GetMapping("/{id}/imagem-direta")
    public ResponseEntity<byte[]> obterImagemDireta(@PathVariable Long id) {
        String imagemBase64 = focoAedesService.obterImagemFoco(id);

        if (imagemBase64 == null) {
            return ResponseEntity.notFound().build();
        }
        String[] partes = imagemBase64.split(",");
        String base64 = partes.length > 1 ? partes[1] : partes[0];
        MediaType mediaType = MediaType.IMAGE_JPEG; // Padrão para JPEG
        if (imagemBase64.contains("image/png")) {
            mediaType = MediaType.IMAGE_PNG;
        } else if (imagemBase64.contains("image/gif")) {
            mediaType = MediaType.IMAGE_GIF;
        }
        byte[] imagemBytes = Base64.getDecoder().decode(base64);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(mediaType);
        return new ResponseEntity<>(imagemBytes, headers, HttpStatus.OK);
    }
}

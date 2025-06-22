package org.example.aces_api.unitests.mapper;

// Importe EntityModel e classes relacionadas ao HATEOAS
import org.example.aces_api.controller.AreaController;
import org.example.aces_api.dto.RelatorioDTO;
import org.example.aces_api.exception.EntityNotFoundException;
import org.example.aces_api.model.entity.Area;
import org.example.aces_api.model.repository.AreaRepository;
import org.example.aces_api.model.repository.CasoDengueRepository;
import org.example.aces_api.model.repository.FocoAedesRepository;
import org.example.aces_api.model.repository.VisitaRepository;
import org.example.aces_api.service.AreaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.hateoas.EntityModel;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RelatorioTest {

    @Mock
    private AreaRepository areaRepository;
    @Mock
    private VisitaRepository visitaRepository;
    @Mock
    private CasoDengueRepository casoDengueRepository;
    @Mock
    private FocoAedesRepository focoAedesRepository;
    @Mock
    private org.example.aces_api.mapper.AreaMapper mapper;

    @InjectMocks
    private AreaService areaService;

    private Area areaMock;
    private Long areaId;
    private LocalDate dataInicio;
    private LocalDate dataFim;

    @BeforeEach
    void setUp() {

        areaId = 1L;
        dataInicio = LocalDate.of(2025, 5, 1);
        dataFim = LocalDate.of(2025, 5, 31);

        areaMock = new Area();
        areaMock.setId(Math.toIntExact(areaId));
        areaMock.setNome("Centro");
    }

    @Test
    @DisplayName("Deve gerar relatório com sucesso e links HATEOAS")
    void deveGerarRelatorioComSucesso() {

        when(areaRepository.findById(Math.toIntExact(areaId))).thenReturn(Optional.of(areaMock));
        when(visitaRepository.countVisitasByAreaAndPeriod(anyLong(), any(LocalDateTime.class), any(LocalDateTime.class))).thenReturn(100);
        when(casoDengueRepository.countDengueByAreaAndPeriod(anyLong(), any(LocalDateTime.class), any(LocalDateTime.class))).thenReturn(10);
        when(focoAedesRepository.countFocosByAreaAndPeriod(anyLong(), any(LocalDateTime.class), any(LocalDateTime.class))).thenReturn(5);

        EntityModel<RelatorioDTO> resultadoModel = areaService.gerarRelatorio(areaId, dataInicio, dataFim);

        assertNotNull(resultadoModel);
        assertNotNull(resultadoModel.getContent());

        RelatorioDTO relatorioDTO = resultadoModel.getContent();
        assertEquals(areaId.intValue(), relatorioDTO.areaId());
        assertEquals("Centro", relatorioDTO.nomeArea());
        assertEquals(10, relatorioDTO.totalCasos());
        assertEquals(100, relatorioDTO.totalVisitas());
        assertEquals(5, relatorioDTO.totalFocos());
        assertEquals(100.0, relatorioDTO.indiceDengue());

        assertTrue(resultadoModel.hasLink("self"));
        assertTrue(resultadoModel.hasLink("area"));
        assertTrue(resultadoModel.getLink("self").get().getHref().contains("/api/areas/1/relatorio"));
        assertTrue(resultadoModel.getLink("area").get().getHref().contains("/api/areas/buscar/1")); // Verifique se este é o path correto
    }

    @Test
    @DisplayName("Deve lançar exceção quando a área não for encontrada")
    void deveLancarExcecaoQuandoAreaNaoEncontrada() {

        when(areaRepository.findById(Math.toIntExact(areaId))).thenReturn(Optional.empty());

        var exception = assertThrows(EntityNotFoundException.class, () -> {
            areaService.gerarRelatorio(areaId, dataInicio, dataFim);
        });

        assertEquals("Área com ID " + areaId + " não encontrada.", exception.getMessage());
    }

    @Test
    @DisplayName("Deve calcular índice de dengue como zero e gerar links HATEOAS")
    void deveCalcularIndiceDengueComoZeroQuandoNaoHaVisitas() {
        when(areaRepository.findById(Math.toIntExact(areaId))).thenReturn(Optional.of(areaMock));
        when(visitaRepository.countVisitasByAreaAndPeriod(anyLong(), any(LocalDateTime.class), any(LocalDateTime.class))).thenReturn(0);
        when(casoDengueRepository.countDengueByAreaAndPeriod(anyLong(), any(LocalDateTime.class), any(LocalDateTime.class))).thenReturn(5);
        when(focoAedesRepository.countFocosByAreaAndPeriod(anyLong(), any(LocalDateTime.class), any(LocalDateTime.class))).thenReturn(2);

        EntityModel<RelatorioDTO> resultadoModel = areaService.gerarRelatorio(areaId, dataInicio, dataFim);

        assertNotNull(resultadoModel);
        assertNotNull(resultadoModel.getContent());

        RelatorioDTO relatorioDTO = resultadoModel.getContent();
        assertEquals(0, relatorioDTO.totalVisitas());
        assertEquals(5, relatorioDTO.totalCasos());
        assertEquals(0.0, relatorioDTO.indiceDengue());
        assertTrue(resultadoModel.hasLink("self"));
        assertTrue(resultadoModel.hasLink("area"));
    }
}
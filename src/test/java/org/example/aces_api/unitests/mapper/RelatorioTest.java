package org.example.aces_api.unitests.mapper;

import org.example.aces_api.dto.RelatorioDTO;
import org.example.aces_api.model.entity.Area;
import org.example.aces_api.model.repository.AreaRepository;
import org.example.aces_api.model.repository.CasoAedesRepository;
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
    private CasoAedesRepository casoAedesRepository;
    @Mock
    private FocoAedesRepository focoAedesRepository;

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
    @DisplayName("Deve gerar relatório com sucesso quando todos os dados estiverem disponíveis")
    void deveGerarRelatorioComSucesso() {

        when(areaRepository.findById(Math.toIntExact(areaId))).thenReturn(Optional.of(areaMock));
        when(visitaRepository.countVisitasByAreaAndPeriod(anyLong(), any(LocalDateTime.class), any(LocalDateTime.class))).thenReturn(100);
        when(casoAedesRepository.countDengueByAreaAndPeriod(anyLong(), any(LocalDateTime.class), any(LocalDateTime.class))).thenReturn(10);
        when(focoAedesRepository.countFocosByAreaAndPeriod(anyLong(), any(LocalDateTime.class), any(LocalDateTime.class))).thenReturn(5);

        RelatorioDTO resultado = areaService.gerarRelatorio(areaId, dataInicio, dataFim);

        assertNotNull(resultado);
        assertEquals(areaId, resultado.areaId());
        assertEquals("Centro", resultado.nomeArea());
        assertEquals(10, resultado.totalCasos());
        assertEquals(100, resultado.totalVisitas());
        assertEquals(5, resultado.totalFocos());
        assertEquals(100.0, resultado.indiceDengue());

        String periodoEsperado = "01/05/2025 a 31/05/2025";
        assertEquals(periodoEsperado, resultado.periodo());
    }

    @Test
    @DisplayName("Deve lançar exceção quando a área não for encontrada")
    void deveLancarExcecaoQuandoAreaNaoEncontrada() {
        when(areaRepository.findById(Math.toIntExact(areaId))).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            areaService.gerarRelatorio(areaId, dataInicio, dataFim);
        });

        assertEquals("Área com ID " + areaId + " não encontrada.", exception.getMessage());
    }

    @Test
    @DisplayName("Deve calcular índice de dengue como zero quando não houver visitas")
    void deveCalcularIndiceDengueComoZeroQuandoNaoHaVisitas() {
        when(areaRepository.findById(Math.toIntExact(areaId))).thenReturn(Optional.of(areaMock));
        when(visitaRepository.countVisitasByAreaAndPeriod(anyLong(), any(LocalDateTime.class), any(LocalDateTime.class))).thenReturn(0);
        when(casoAedesRepository.countDengueByAreaAndPeriod(anyLong(), any(LocalDateTime.class), any(LocalDateTime.class))).thenReturn(5); // Mesmo com casos
        when(focoAedesRepository.countFocosByAreaAndPeriod(anyLong(), any(LocalDateTime.class), any(LocalDateTime.class))).thenReturn(2);

        RelatorioDTO resultado = areaService.gerarRelatorio(areaId, dataInicio, dataFim);

        assertNotNull(resultado);
        assertEquals(0, resultado.totalVisitas());
        assertEquals(5, resultado.totalCasos());
        assertEquals(0.0, resultado.indiceDengue());
    }
}

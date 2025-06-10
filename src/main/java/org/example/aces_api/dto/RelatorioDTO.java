package org.example.aces_api.dto;

import java.time.LocalDateTime;

public class RelatorioDTO {

    private Long areaId;
    private String nomeArea;
    private String periodo;
    private long totalCasos;
    private long totalVisitas;
    private double indiceDengue;
    private LocalDateTime dataGeracao;

    public RelatorioDTO() {
    }

    public RelatorioDTO(Long areaId, String nomeArea, String periodo,
                        long totalCasos, long totalVisitas, double indiceDengue, LocalDateTime dataGeracao) {
        this.areaId = areaId;
        this.nomeArea = nomeArea;
        this.periodo = periodo;
        this.totalCasos = totalCasos;
        this.totalVisitas = totalVisitas;
        this.indiceDengue = indiceDengue;
        this.dataGeracao = dataGeracao;
    }

    public Long getAreaId() {
        return areaId;
    }

    public void setAreaId(Long areaId) {
        this.areaId = areaId;
    }

    public String getNomeArea() {
        return nomeArea;
    }

    public void setNomeArea(String nomeArea) {
        this.nomeArea = nomeArea;
    }

    public String getPeriodo() {
        return periodo;
    }

    public void setPeriodo(String periodo) {
        this.periodo = periodo;
    }

    public long getTotalCasos() {
        return totalCasos;
    }

    public void setTotalCasos(long totalCasos) {
        this.totalCasos = totalCasos;
    }

    public long getTotalVisitas() {
        return totalVisitas;
    }

    public void setTotalVisitas(long totalVisitas) {
        this.totalVisitas = totalVisitas;
    }

    public double getIndiceDengue() {
        return indiceDengue;
    }

    public void setIndiceDengue(double indiceDengue) {
        this.indiceDengue = indiceDengue;
    }

    public LocalDateTime getDataGeracao() {
        return dataGeracao;
    }

    public void setDataGeracao(LocalDateTime dataGeracao) {
        this.dataGeracao = dataGeracao;
    }
}

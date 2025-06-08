package org.example.aces_api.model.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name= "relatorios")
public class Relatorio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    @ManyToOne
//    @JoinColumn(name = "area_id")
//    private Area area;

    private String periodo;
    private long totalCasos;
    private long totalVisitas;
    private double indiceDengue;
    private LocalDateTime dataGeracao;

    public Relatorio() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

//    public Area getArea() {
//        return area;
//    }
//
//    public void setArea(Area area) {
//        this.area = area;
//    }

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

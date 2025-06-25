package org.example.aces_api.model.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.math.BigDecimal;

@Entity
@Table(name = "Visita")
public class Visita {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne
    @JoinColumn(name = "agente_id")
    private Agente agente;
    @ManyToOne
    @JoinColumn(name = "endereco_id")
    private Endereco endereco;

    private LocalDateTime dataHora;
    private String observacoes;
    private String status;
    private BigDecimal temperatura;
    private boolean foiRealizada;

    public Visita() {
    }

    public Visita(int id, Agente agente, Endereco endereco, LocalDateTime dataHora, String observacoes, String status, BigDecimal temperatura, boolean foiRealizada) {
        this.id = id;
        this.agente = agente;
        this.endereco = endereco;
        this.dataHora = dataHora;
        this.observacoes = observacoes;
        this.status = status;
        this.temperatura = temperatura;
        this.foiRealizada = foiRealizada;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Agente getAgente() {
        return agente;
    }

    public void setAgente(Agente agente) {
        this.agente = agente;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BigDecimal getTemperatura() {
        return temperatura;
    }

    public void setTemperatura(BigDecimal temperatura) {
        this.temperatura = temperatura;
    }

    public boolean isFoiRealizada() {
        return foiRealizada;
    }

    public void setFoiRealizada(boolean foiRealizada) {
        this.foiRealizada = foiRealizada;
    }

}

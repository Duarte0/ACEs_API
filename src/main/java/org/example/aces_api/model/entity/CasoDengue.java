package org.example.aces_api.model.entity;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "CasoDengue")
public class CasoDengue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "visita_id")
    private Visita visita;

    @ManyToOne
    @JoinColumn(name = "paciente_id")
    private Paciente paciente;

    private LocalDate dataDiagnostico;
    private String tipoDengue;
    private String gravidade;
    private String sintomas;
    private String observacoes;
    private boolean confirmadoLab;

    public CasoDengue() {
    }

    public CasoDengue(int id, Visita visita, Paciente paciente, LocalDate dataDiagnostico, String tipoDengue, String gravidade, String sintomas, String observacoes, boolean confirmadoLab) {
        this.id = id;
        this.visita = visita;
        this.paciente = paciente;
        this.dataDiagnostico = dataDiagnostico;
        this.tipoDengue = tipoDengue;
        this.gravidade = gravidade;
        this.sintomas = sintomas;
        this.observacoes = observacoes;
        this.confirmadoLab = confirmadoLab;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Visita getVisita() {
        return visita;
    }

    public void setVisita(Visita visita) {
        this.visita = visita;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

    public LocalDate getDataDiagnostico() {
        return dataDiagnostico;
    }

    public void setDataDiagnostico(LocalDate dataDiagnostico) {
        this.dataDiagnostico = dataDiagnostico;
    }

    public String getTipoDengue() {
        return tipoDengue;
    }

    public void setTipoDengue(String tipoDengue) {
        this.tipoDengue = tipoDengue;
    }

    public String getGravidade() {
        return gravidade;
    }

    public void setGravidade(String gravidade) {
        this.gravidade = gravidade;
    }

    public String getSintomas() {
        return sintomas;
    }

    public void setSintomas(String sintomas) {
        this.sintomas = sintomas;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    public boolean isConfirmadoLab() {
        return confirmadoLab;
    }

    public void setConfirmadoLab(boolean confirmadoLab) {
        this.confirmadoLab = confirmadoLab;
    }
}
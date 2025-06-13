package org.example.aces_api.model.entity;

import jakarta.persistence.*;
import org.example.aces_api.dto.Risco;

import java.time.LocalDateTime;

@Entity
@Table(name = "Area")
public class Area {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String nome;
    private String descricao;
    private String regiao;
    private int populacaoAprox;
    private Risco nivelRisco;
    private Risco prioridade;
    private LocalDateTime dataUltimaAtt;

    public Area() {
    }

    public Area(String nome, String descricao, String regiao, int populacaoAprox, Risco nivelRisco, Risco prioridade, LocalDateTime dataUltimaAtt) {
        this.nome = nome;
        this.descricao = descricao;
        this.regiao = regiao;
        this.populacaoAprox = populacaoAprox;
        this.nivelRisco = nivelRisco;
        this.prioridade = prioridade;
        this.dataUltimaAtt = dataUltimaAtt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getRegiao() {
        return regiao;
    }

    public void setRegiao(String regiao) {
        this.regiao = regiao;
    }

    public int getPopulacaoAprox() {
        return populacaoAprox;
    }

    public void setPopulacaoAprox(int populacaoAprox) {
        this.populacaoAprox = populacaoAprox;
    }

    public Risco getNivelRisco() {
        return nivelRisco;
    }

    public void setNivelRisco(Risco nivelRisco) {
        this.nivelRisco = nivelRisco;
    }

    public Risco getPrioridade() {
        return prioridade;
    }

    public void setPrioridade(Risco prioridade) {
        this.prioridade = prioridade;
    }

    public LocalDateTime getDataUltimaAtt() {
        return dataUltimaAtt;
    }

    public void setDataUltimaAtt(LocalDateTime dataUltimaAtt) {
        this.dataUltimaAtt = dataUltimaAtt;
    }
}

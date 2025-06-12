package org.example.aces_api.model.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "FocoAedes")
public class FocoAedes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "visita_id")
    private Visita visita;

    private String imagem;
    private String tipoFoco;
    private int quantidade;
    private boolean tratado;
    private String observacoes;

    public FocoAedes() {
    }

    public FocoAedes(int id, Visita visita, String imagem, String tipoFoco, int quantidade, boolean tratado, String observacoes) {
        this.id = (long) id;
        this.visita = visita;
        this.imagem = imagem;
        this.tipoFoco = tipoFoco;
        this.quantidade = quantidade;
        this.tratado = tratado;
        this.observacoes = observacoes;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Visita getVisita() {
        return visita;
    }

    public void setVisita(Visita visita) {
        this.visita = visita;
    }

    public String getImagem() {
        return imagem;
    }

    public void setImagem(String imagem) {
        this.imagem = imagem;
    }

    public String getTipoFoco() {
        return tipoFoco;
    }

    public void setTipoFoco(String tipoFoco) {
        this.tipoFoco = tipoFoco;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public boolean isTratado() {
        return tratado;
    }

    public void setTratado(boolean tratado) {
        this.tratado = tratado;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }
}

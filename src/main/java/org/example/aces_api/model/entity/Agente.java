package org.example.aces_api.model.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "Agente")
public class Agente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false, unique = true)
    private Usuario usuario;

    private String matricula;
    private LocalDate dataAdmissao;
    private LocalDate dataInicio;
    private LocalDate dataFim;
    private Boolean ativo = true;

    @OneToMany(mappedBy = "agente", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<AgenteArea> areas = new HashSet<>();

    public Agente() {}

    public Agente(Usuario usuario, String matricula) {
        this.usuario = usuario;
        this.matricula = matricula;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public LocalDate getDataAdmissao() {
        return dataAdmissao;
    }

    public void setDataAdmissao(LocalDate dataAdmissao) {
        this.dataAdmissao = dataAdmissao;
    }

    public LocalDate getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(LocalDate dataInicio) {
        this.dataInicio = dataInicio;
    }

    public LocalDate getDataFim() {
        return dataFim;
    }

    public void setDataFim(LocalDate dataFim) {
        if (dataInicio != null && dataFim != null && dataFim.isBefore(dataInicio)) {
            throw new IllegalArgumentException("Data fim não pode ser anterior à data de início");
        }
        this.dataFim = dataFim;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public Set<AgenteArea> getAreas() {
        return areas;
    }

    public void setAreas(Set<AgenteArea> areas) {
        this.areas = areas;
    }

    /*
    public void adicionarArea(Area area) {
        AgenteArea agenteArea = new AgenteArea(this, area);
        areas.add(agenteArea);
        area.getAgentes().add(agenteArea);
    }

    public void removerArea(Area area) {
        AgenteArea agenteArea = new AgenteArea(this, area);
        area.getAgentes().remove(agenteArea);
        areas.remove(agenteArea);
    }

     */
}
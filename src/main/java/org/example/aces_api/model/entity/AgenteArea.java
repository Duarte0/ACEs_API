package org.example.aces_api.model.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "AgenteArea")
public class AgenteArea {

    @EmbeddedId
    private AgenteAreaId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("agenteId")
    @JoinColumn(name = "agente_id")
    private Agente agente;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("areaId")
    @JoinColumn(name = "area_id")
    private Area area;

    public AgenteArea() {}

    public AgenteArea(Agente agente, Area area) {
        this.agente = agente;
        this.area = area;
        this.id = new AgenteAreaId(agente.getId(), area.getId());
    }

    // Getters e Setters
    public AgenteAreaId getId() {
        return id;
    }

    public void setId(AgenteAreaId id) {
        this.id = id;
    }

    public Agente getAgente() {
        return agente;
    }

    public void setAgente(Agente agente) {
        this.agente = agente;
    }

    public Area getArea() {
        return area;
    }

    public void setArea(Area area) {
        this.area = area;
    }
}
package org.example.aces_api.model.entity;

import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class AgenteAreaId implements Serializable {

    private Integer agenteId;
    private Integer areaId;

    public AgenteAreaId() {}

    public AgenteAreaId(Integer agenteId, Integer areaId) {
        this.agenteId = agenteId;
        this.areaId = areaId;
    }

    public Integer getAgenteId() {
        return agenteId;
    }

    public void setAgenteId(Integer agenteId) {
        this.agenteId = agenteId;
    }

    public Integer getAreaId() {
        return areaId;
    }

    public void setAreaId(Integer areaId) {
        this.areaId = areaId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AgenteAreaId that = (AgenteAreaId) o;
        return Objects.equals(agenteId, that.agenteId) &&
                Objects.equals(areaId, that.areaId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(agenteId, areaId);
    }
}
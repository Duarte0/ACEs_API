package org.example.aces_api.dto;

public enum TipoFoco {
    CAIXA_DAGUA("Caixa d'água"),
    PNEU("Pneu"),
    VASO_PLANTA("Vaso de planta"),
    GARRAFA("Garrafa"),
    PISCINA("Piscina"),
    CALHA("Calha"),
    LIXO("Lixo acumulado"),
    RECIPIENTE_PLASTICO("Recipiente plástico"),
    BROMELIA("Bromélia"),
    BEBEDOURO("Bebedouro de animais"),
    LAJE("Laje"),
    RALO("Ralo"),
    OUTRO("Outro");

    private final String descricao;

    TipoFoco(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

    @Override
    public String toString() {
        return descricao;
    }

    public static TipoFoco fromString(String texto) {
        for (TipoFoco tipo : TipoFoco.values()) {
            if (tipo.descricao.equalsIgnoreCase(texto)) {
                return tipo;
            }
        }
        return OUTRO;
    }
}

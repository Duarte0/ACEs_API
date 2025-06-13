package org.example.aces_api.dto;

public enum Risco {
    BAIXO("Baixo"),
    MEDIO("Médio"),
    ALTO("Alto"),
    MUITO_ALTO("Muito Alto");

    private final String descricao;

    Risco(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

    // Opcional: Método estático para obter o enum a partir da descrição
    public static Risco fromDescricao(String descricao) {
        for (Risco risco : Risco.values()) {
            if (risco.descricao.equalsIgnoreCase(descricao)) {
                return risco;
            }
        }
        throw new IllegalArgumentException("Nenhum Risco encontrado com a descrição: " + descricao);
    }
}

package org.example.aces_api.dto;

public enum Sexo {

    MASCULINO("Masculino"),
    FEMININO("Feminino"),
    OUTROS("Outros");

    private final String descricao;

    Sexo(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

    // Opcional: Método estático para obter o enum a partir da descrição
    public static Sexo fromDescricao(String descricao) {
        for (Sexo risco : Sexo.values()) {
            if (risco.descricao.equalsIgnoreCase(descricao)) {
                return risco;
            }
        }
        throw new IllegalArgumentException("Nenhum Sexo encontrado com a descrição: " + descricao);
    }
}

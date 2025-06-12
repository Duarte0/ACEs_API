package org.example.aces_api.dto;

public enum TipoDengue {
    DENGUE_CLASSICA("Dengue Clássica"),
    DENGUE_COM_SINAIS_DE_ALARME("Dengue com Sinais de Alarme"),
    DENGUE_GRAVE("Dengue Grave"),
    DENGUE_TIPO_1("Dengue Sorotipo 1"),
    DENGUE_TIPO_2("Dengue Sorotipo 2"),
    DENGUE_TIPO_3("Dengue Sorotipo 3"),
    DENGUE_TIPO_4("Dengue Sorotipo 4"),
    DENGUE_PRIMARIA("Dengue Primária"),
    DENGUE_SECUNDARIA("Dengue Secundária"),
    NAO_ESPECIFICADO("Não Especificado");

    private final String descricao;

    TipoDengue(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

    @Override
    public String toString() {
        return descricao;
    }

    // Método utilitário para converter String para enum
    public static TipoDengue fromString(String texto) {
        for (TipoDengue tipo : TipoDengue.values()) {
            if (tipo.descricao.equalsIgnoreCase(texto)) {
                return tipo;
            }
        }
        return NAO_ESPECIFICADO;
    }
}

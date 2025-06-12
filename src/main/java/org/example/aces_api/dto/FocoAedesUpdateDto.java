package org.example.aces_api.dto;


public record FocoAedesUpdateDto(
        TipoFoco tipoFoco,
        Integer quantidade,
        Boolean tratado,
        String observacoes
) {

    public FocoAedesUpdateDto() {
        this(null, null, null, null);
    }


    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private TipoFoco tipoFoco;
        private Integer quantidade;
        private Boolean tratado;
        private String observacoes;

        public Builder tipoFoco(TipoFoco tipoFoco) {
            this.tipoFoco = tipoFoco;
            return this;
        }

        public Builder quantidade(Integer quantidade) {
            this.quantidade = quantidade;
            return this;
        }

        public Builder tratado(Boolean tratado) {
            this.tratado = tratado;
            return this;
        }

        public Builder observacoes(String observacoes) {
            this.observacoes = observacoes;
            return this;
        }

        public FocoAedesUpdateDto build() {
            return new FocoAedesUpdateDto(tipoFoco, quantidade, tratado, observacoes);
        }
    }

    // Métodos para verificar campos preenchidos
    public boolean hasTipoFoco() {
        return tipoFoco != null;
    }

    public boolean hasQuantidade() {
        return quantidade != null;
    }

    public boolean hasTratado() {
        return tratado != null;
    }

    public boolean hasObservacoes() {
        return observacoes != null;
    }
}
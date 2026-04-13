package com.pucminas.aluguelcarros.model;

/**
 * Status de um pedido de aluguel
 */
public enum StatusPedido {
    CRIADO("Criado", "Novo pedido criado pelo cliente"),
    ENVIADO_ANALISE("Aguardando Análise", "Pedido enviado para análise financeira do agente"),
    APROVADO("Aprovado", "Pedido aprovado para execução do contrato"),
    REJEITADO("Rejeitado", "Pedido rejeitado pelo agente"),
    CANCELADO_CLIENTE("Cancelado", "Pedido cancelado pelo cliente");

    private final String label;
    private final String descricao;

    StatusPedido(String label, String descricao) {
        this.label = label;
        this.descricao = descricao;
    }

    public String getLabel() {
        return label;
    }

    public String getDescricao() {
        return descricao;
    }

    public String getCorBadge() {
        return switch (this) {
            case CRIADO -> "#007bff";           // Azul
            case ENVIADO_ANALISE -> "#ffc107";  // Amarelo
            case APROVADO -> "#28a745";         // Verde
            case REJEITADO -> "#dc3545";        // Vermelho
            case CANCELADO_CLIENTE -> "#6c757d"; // Cinza
        };
    }
}

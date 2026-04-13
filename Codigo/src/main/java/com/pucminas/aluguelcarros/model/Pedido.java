package com.pucminas.aluguelcarros.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Entidade Pedido de Aluguel
 * Representa um pedido de aluguel criado por um cliente
 */
@Entity
@Table(name = "pedido")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Cliente é obrigatório")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @NotNull(message = "Automóvel é obrigatório")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "automovel_id", nullable = false)
    private Automovel automovel;

    @NotNull(message = "Data de início é obrigatória")
    @Future(message = "Data de início deve ser no futuro")
    @Column(nullable = false)
    private LocalDate dataInicio;

    @NotNull(message = "Data de fim é obrigatória")
    @Column(nullable = false)
    private LocalDate dataFim;

    @NotNull(message = "Status é obrigatório")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusPedido status = StatusPedido.CRIADO;

    @NotNull(message = "Valor diário é obrigatório")
    @DecimalMin(value = "0.01", message = "Valor diário deve ser maior que zero")
    @Column(nullable = false)
    private BigDecimal valorDiario;

    @Column(nullable = true, length = 500)
    private String motivoRejeicao;

    @Column(nullable = false)
    private LocalDateTime dataCriacao = LocalDateTime.now();

    @Column(nullable = false)
    private LocalDateTime dataAtualizacao = LocalDateTime.now();

    @PreUpdate
    public void preUpdate() {
        this.dataAtualizacao = LocalDateTime.now();
    }

    /**
     * Calcula o valor total do aluguel
     */
    public BigDecimal calcularValorTotal() {
        if (dataInicio != null && dataFim != null && valorDiario != null) {
            long dias = java.time.temporal.ChronoUnit.DAYS.between(dataInicio, dataFim);
            return valorDiario.multiply(BigDecimal.valueOf(dias));
        }
        return BigDecimal.ZERO;
    }

    /**
     * Calcula a quantidade de dias de aluguel
     */
    public Long obterQuantidadeDias() {
        if (dataInicio != null && dataFim != null) {
            return java.time.temporal.ChronoUnit.DAYS.between(dataInicio, dataFim);
        }
        return 0L;
    }

    @Override
    public String toString() {
        return "Pedido{" +
                "id=" + id +
                ", cliente=" + cliente.getNome() +
                ", automovel=" + automovel +
                ", dataInicio=" + dataInicio +
                ", dataFim=" + dataFim +
                ", status=" + status +
                '}';
    }
}

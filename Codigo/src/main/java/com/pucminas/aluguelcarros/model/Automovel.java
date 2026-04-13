package com.pucminas.aluguelcarros.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entidade Automóvel - Carros disponíveis para aluguel
 */
@Entity
@Table(name = "automovel")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Automovel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Marca é obrigatória")
    @Column(nullable = false)
    private String marca;

    @NotBlank(message = "Modelo é obrigatório")
    @Column(nullable = false)
    private String modelo;

    @NotBlank(message = "Placa é obrigatória")
    @Column(nullable = false, unique = true, length = 10)
    private String placa;

    @NotNull(message = "Ano de fabricação é obrigatório")
    @Min(value = 1900, message = "Ano deve ser maior que 1900")
    @Max(value = 2100, message = "Ano deve ser menor que 2100")
    @Column(nullable = false)
    private Integer anoFabricacao;

    @NotNull(message = "Tipo de proprietário é obrigatório")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoUsuario proprietarioTipo;

    @NotNull(message = "ID do proprietário é obrigatório")
    @Column(nullable = false)
    private Long proprietarioId;

    @Positive(message = "Valor diário deve ser positivo")
    @Column(nullable = true)
    private java.math.BigDecimal valorDiarioPadrao;

    @Column(nullable = true)
    private String descricao;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private java.time.LocalDateTime dataCriacao = java.time.LocalDateTime.now();

    @Override
    public String toString() {
        return marca + " " + modelo + " (" + anoFabricacao + ") - " + placa;
    }
}

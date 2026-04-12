package com.pucminas.aluguelcarros.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "entidade_empregadora")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EntidadeEmpregadora {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Nome da entidade empregadora é obrigatório")
    @Column(nullable = false)
    private String nomeEntidade;

    @NotNull(message = "Rendimento é obrigatório")
    @Positive(message = "Rendimento deve ser positivo")
    @Column(nullable = false)
    private BigDecimal rendimento;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;
}

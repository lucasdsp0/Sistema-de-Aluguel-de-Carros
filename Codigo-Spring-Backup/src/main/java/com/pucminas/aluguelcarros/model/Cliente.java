package com.pucminas.aluguelcarros.model;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.br.CPF;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "cliente")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Nome é obrigatório")
    @Column(nullable = false)
    private String nome;

    @NotBlank(message = "CPF é obrigatório")
    @CPF(message = "CPF inválido")
    @Column(nullable = false, unique = true, length = 14)
    private String cpf;

    @NotBlank(message = "RG é obrigatório")
    @Column(nullable = false, length = 20)
    private String rg;

    @NotBlank(message = "Endereço é obrigatório")
    @Column(nullable = false)
    private String endereco;

    @NotBlank(message = "Profissão é obrigatória")
    @Column(nullable = false)
    private String profissao;

    // Login
    @NotBlank(message = "E-mail é obrigatório")
    @Email(message = "E-mail inválido")
    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String senha; // armazenada com hash BCrypt

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoUsuario tipoUsuario = TipoUsuario.CLIENTE;

    @Valid
    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @Size(max = 3, message = "Máximo de 3 entidades empregadoras permitidas")
    private List<EntidadeEmpregadora> entidadesEmpregadoras = new ArrayList<>();

    // Helpers para gerenciar o relacionamento
    public void addEntidadeEmpregadora(EntidadeEmpregadora entidade) {
        entidade.setCliente(this);
        this.entidadesEmpregadoras.add(entidade);
    }

    public void clearEntidadesEmpregadoras() {
        this.entidadesEmpregadoras.forEach(e -> e.setCliente(null));
        this.entidadesEmpregadoras.clear();
    }
}

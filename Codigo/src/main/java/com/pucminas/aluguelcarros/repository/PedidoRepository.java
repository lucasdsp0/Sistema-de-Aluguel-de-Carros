package com.pucminas.aluguelcarros.repository;

import com.pucminas.aluguelcarros.model.Pedido;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

/**
 * Repositório para Pedido usando Panache
 */
@ApplicationScoped
public class PedidoRepository implements PanacheRepository<Pedido> {
    // Métodos customizados podem ser adicionados depois
}

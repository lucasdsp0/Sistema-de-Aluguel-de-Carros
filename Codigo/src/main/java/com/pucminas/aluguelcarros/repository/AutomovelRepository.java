package com.pucminas.aluguelcarros.repository;

import com.pucminas.aluguelcarros.model.Automovel;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.Optional;

/**
 * Repositório para Automóvel usando Panache
 */
@ApplicationScoped
public class AutomovelRepository implements PanacheRepository<Automovel> {

    /**
     * Buscar automóvel por placa
     */
    public Optional<Automovel> findByPlaca(String placa) {
        return find("placa", placa).firstResultOptional();
    }
}

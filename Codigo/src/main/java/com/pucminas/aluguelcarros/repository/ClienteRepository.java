package com.pucminas.aluguelcarros.repository;

import com.pucminas.aluguelcarros.model.Cliente;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Optional;

/**
 * Repository para Cliente usando Quarkus Panache
 */
@ApplicationScoped
public class ClienteRepository implements PanacheRepository<Cliente> {

    public Optional<Cliente> findByEmail(String email) {
        return find("email", email).firstResultOptional();
    }

    public Optional<Cliente> findByCpf(String cpf) {
        return find("cpf", cpf).firstResultOptional();
    }

    public boolean existsByCpf(String cpf) {
        return find("cpf", cpf).firstResultOptional().isPresent();
    }

    public boolean existsByEmail(String email) {
        return find("email", email).firstResultOptional().isPresent();
    }
}

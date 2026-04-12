package com.pucminas.aluguelcarros.service;

import com.pucminas.aluguelcarros.model.Cliente;
import com.pucminas.aluguelcarros.model.EntidadeEmpregadora;
import com.pucminas.aluguelcarros.repository.ClienteRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;
import java.util.Optional;

/**
 * Serviço de negócio para Cliente com Quarkus
 */
@ApplicationScoped
@Transactional
public class ClienteService {

    @Inject
    ClienteRepository clienteRepository;

    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // -------------------------------------------------------
    // CREATE
    // -------------------------------------------------------
    public Cliente cadastrar(Cliente cliente) {
        if (clienteRepository.existsByCpf(cliente.getCpf())) {
            throw new IllegalArgumentException("CPF já cadastrado no sistema.");
        }
        if (clienteRepository.existsByEmail(cliente.getEmail())) {
            throw new IllegalArgumentException("E-mail já cadastrado no sistema.");
        }
        if (cliente.getEntidadesEmpregadoras().size() > 3) {
            throw new IllegalArgumentException("Máximo de 3 entidades empregadoras permitidas.");
        }

        // Criptografar a senha antes de salvar
        cliente.setSenha(passwordEncoder.encode(cliente.getSenha()));

        // Garantir o vínculo bidirecional com as entidades empregadoras
        for (EntidadeEmpregadora e : cliente.getEntidadesEmpregadoras()) {
            e.setCliente(cliente);
        }

        return clienteRepository.save(cliente);
    }

    // -------------------------------------------------------
    // READ
    // -------------------------------------------------------
    @Transactional(value = Transactional.TxType.SUPPORTS)
    public List<Cliente> listarTodos() {
        return clienteRepository.findAll();
    }

    @Transactional(value = Transactional.TxType.SUPPORTS)
    public Optional<Cliente> buscarPorId(Long id) {
        return clienteRepository.findById(id);
    }

    @Transactional(value = Transactional.TxType.SUPPORTS)
    public Optional<Cliente> buscarPorEmail(String email) {
        return clienteRepository.findByEmail(email);
    }

    // -------------------------------------------------------
    // UPDATE
    // -------------------------------------------------------
    public Cliente atualizar(Long id, Cliente dadosAtualizados) {
        Cliente clienteExistente = clienteRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado."));

        // Verificar unicidade do CPF (se foi alterado)
        if (!clienteExistente.getCpf().equals(dadosAtualizados.getCpf())
                && clienteRepository.existsByCpf(dadosAtualizados.getCpf())) {
            throw new IllegalArgumentException("CPF já cadastrado para outro cliente.");
        }

        // Verificar unicidade do e-mail (se foi alterado)
        if (!clienteExistente.getEmail().equals(dadosAtualizados.getEmail())
                && clienteRepository.existsByEmail(dadosAtualizados.getEmail())) {
            throw new IllegalArgumentException("E-mail já cadastrado para outro cliente.");
        }

        if (dadosAtualizados.getEntidadesEmpregadoras().size() > 3) {
            throw new IllegalArgumentException("Máximo de 3 entidades empregadoras permitidas.");
        }

        // Atualizar campos
        clienteExistente.setNome(dadosAtualizados.getNome());
        clienteExistente.setCpf(dadosAtualizados.getCpf());
        clienteExistente.setRg(dadosAtualizados.getRg());
        clienteExistente.setEndereco(dadosAtualizados.getEndereco());
        clienteExistente.setProfissao(dadosAtualizados.getProfissao());
        clienteExistente.setEmail(dadosAtualizados.getEmail());

        // Atualizar senha apenas se foi fornecida
        if (dadosAtualizados.getSenha() != null && !dadosAtualizados.getSenha().isBlank()) {
            clienteExistente.setSenha(passwordEncoder.encode(dadosAtualizados.getSenha()));
        }

        // Atualizar entidades empregadoras (remove antigas, adiciona novas)
        clienteExistente.clearEntidadesEmpregadoras();
        for (EntidadeEmpregadora e : dadosAtualizados.getEntidadesEmpregadoras()) {
            e.setCliente(clienteExistente);
            clienteExistente.getEntidadesEmpregadoras().add(e);
        }

        return clienteRepository.save(clienteExistente);
    }

    // -------------------------------------------------------
    // DELETE
    // -------------------------------------------------------
    public void excluir(Long id) {
        if (!clienteRepository.existsById(id)) {
            throw new IllegalArgumentException("Cliente não encontrado.");
        }
        clienteRepository.deleteById(id);
    }
}

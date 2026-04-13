package com.pucminas.aluguelcarros.service;

import com.pucminas.aluguelcarros.model.Automovel;
import com.pucminas.aluguelcarros.model.Pedido;
import com.pucminas.aluguelcarros.model.StatusPedido;
import com.pucminas.aluguelcarros.repository.AutomovelRepository;
import com.pucminas.aluguelcarros.repository.PedidoRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Serviço de negócio para Automóvel
 */
@ApplicationScoped
@Transactional
public class AutomovelService {

    @Inject
    AutomovelRepository automovelRepository;

    @Inject
    PedidoRepository pedidoRepository;

    /**
     * Criar automóvel
     */
    public Automovel criar(Automovel automovel) {
        if (automovelRepository.findByPlaca(automovel.getPlaca()).isPresent()) {
            throw new IllegalArgumentException("Placa já existe: " + automovel.getPlaca());
        }
        automovelRepository.persist(automovel);
        return automovel;
    }

    /**
     * Listar todos os automóveis
     */
    @Transactional(Transactional.TxType.SUPPORTS)
    public List<Automovel> listarTodos() {
        return automovelRepository.listAll();
    }

    /**
     * Buscar por ID
     */
    @Transactional(Transactional.TxType.SUPPORTS)
    public Optional<Automovel> buscarPorId(Long id) {
        return automovelRepository.findByIdOptional(id);
    }

    /**
     * Buscar por Placa
     */
    @Transactional(Transactional.TxType.SUPPORTS)
    public Optional<Automovel> buscarPorPlaca(String placa) {
        return automovelRepository.findByPlaca(placa);
    }

    /**
     * Atualizar automóvel
     */
    public Automovel atualizar(Long id, Automovel dataAtualizada) {
        Automovel existente = automovelRepository.findById(id);
        if (existente == null) {
            throw new IllegalArgumentException("Automóvel não encontrado: " + id);
        }
        existente.setMarca(dataAtualizada.getMarca());
        existente.setModelo(dataAtualizada.getModelo());
        existente.setAnoFabricacao(dataAtualizada.getAnoFabricacao());
        existente.setValorDiarioPadrao(dataAtualizada.getValorDiarioPadrao());
        existente.setDescricao(dataAtualizada.getDescricao());
        return existente;
    }

    /**
     * Verifica se automóvel está disponível no período
     * @return true se disponível, false caso contrário
     */
    @Transactional(Transactional.TxType.SUPPORTS)
    public boolean verificarDisponibilidade(Long automovelId, LocalDate dataInicio, LocalDate dataFim) {
        if (dataFim.isBefore(dataInicio)) {
            return false;
        }

        // Buscar pedidos APROVADOS que conflitem com as datas
        List<Pedido> pedidosConflitantes = pedidoRepository.find(
            "automovel.id = ?1 AND status = ?2 AND dataInicio <= ?3 AND dataFim >= ?4",
            automovelId,
            StatusPedido.APROVADO,
            dataFim,
            dataInicio
        ).list();

        return pedidosConflitantes.isEmpty();
    }

    /**
     * Excluir automóvel
     */
    public void excluir(Long id) {
        Automovel automovel = automovelRepository.findById(id);
        if (automovel == null) {
            throw new IllegalArgumentException("Automóvel não encontrado: " + id);
        }
        automovelRepository.deleteById(id);
    }
}

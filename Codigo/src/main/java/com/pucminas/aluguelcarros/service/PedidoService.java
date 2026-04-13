package com.pucminas.aluguelcarros.service;

import com.pucminas.aluguelcarros.model.Automovel;
import com.pucminas.aluguelcarros.model.Cliente;
import com.pucminas.aluguelcarros.model.Pedido;
import com.pucminas.aluguelcarros.model.StatusPedido;
import com.pucminas.aluguelcarros.model.TipoUsuario;
import com.pucminas.aluguelcarros.repository.AutomovelRepository;
import com.pucminas.aluguelcarros.repository.PedidoRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Serviço de negócio para Pedido de Aluguel
 */
@ApplicationScoped
@Transactional
public class PedidoService {

    @Inject
    PedidoRepository pedidoRepository;

    @Inject
    AutomovelRepository automovelRepository;

    @Inject
    AutomovelService automovelService;

    /**
     * Criar novo pedido com todas as validações
     */
    public Pedido criarPedido(Cliente cliente, Automovel automovel, LocalDate dataInicio,
                              LocalDate dataFim, BigDecimal valorDiario) {

        if (cliente == null) {
            throw new IllegalArgumentException("Cliente é obrigatório");
        }

        if (automovel == null) {
            throw new IllegalArgumentException("Automóvel é obrigatório");
        }

        if (dataInicio == null) {
            throw new IllegalArgumentException("Data de início é obrigatória");
        }

        if (dataFim == null) {
            throw new IllegalArgumentException("Data de fim é obrigatória");
        }

        if (dataFim.isBefore(dataInicio) || dataFim.isEqual(dataInicio)) {
            throw new IllegalArgumentException("Data de fim deve ser posterior à data de início");
        }

        if (dataInicio.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Data de início não pode ser no passado");
        }

        if (valorDiario == null || valorDiario.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Valor diário deve ser maior que zero");
        }

        // Verificar disponibilidade do automóvel
        if (!automovelService.verificarDisponibilidade(automovel.getId(), dataInicio, dataFim)) {
            throw new IllegalArgumentException("Automóvel não está disponível no período solicitado");
        }

        // Criar novo pedido
        Pedido pedido = new Pedido();
        pedido.setCliente(cliente);
        pedido.setAutomovel(automovel);
        pedido.setDataInicio(dataInicio);
        pedido.setDataFim(dataFim);
        pedido.setValorDiario(valorDiario);
        pedido.setStatus(StatusPedido.ENVIADO_ANALISE);

        pedidoRepository.persist(pedido);
        return pedido;
    }

    /**
     * Listar todos os pedidos de um cliente
     */
    @Transactional(Transactional.TxType.SUPPORTS)
    public List<Pedido> listarPedidosCliente(Cliente cliente) {
        if (cliente == null || cliente.getId() == null) {
            throw new IllegalArgumentException("Cliente inválido");
        }
        return pedidoRepository.find("cliente.id = ?1 ORDER BY dataCriacao DESC", cliente.getId()).list();
    }

    /**
     * Listar pedidos aguardando validação (status = ENVIADO_ANALISE)
     */
    @Transactional(Transactional.TxType.SUPPORTS)
    public List<Pedido> listarPedidosParaValidacao() {
        return pedidoRepository.find("status = ?1 ORDER BY dataCriacao ASC", StatusPedido.ENVIADO_ANALISE).list();
    }

    /**
     * Buscar pedido por ID
     */
    @Transactional(Transactional.TxType.SUPPORTS)
    public Optional<Pedido> buscarPorId(Long id) {
        return pedidoRepository.findByIdOptional(id);
    }

    /**
     * Cancelar pedido (apenas cliente pode cancelar)
     */
    public void cancelarPedido(Long pedidoId, Cliente clienteLogado) {
        if (pedidoId == null) {
            throw new IllegalArgumentException("ID do pedido é obrigatório");
        }

        if (clienteLogado == null) {
            throw new IllegalArgumentException("Usuário não autenticado");
        }

        Pedido pedido = pedidoRepository.findById(pedidoId);
        if (pedido == null) {
            throw new IllegalArgumentException("Pedido não encontrado: " + pedidoId);
        }

        // Apenas o próprio cliente pode cancelar seu pedido
        if (!pedido.getCliente().getId().equals(clienteLogado.getId())) {
            throw new IllegalArgumentException("Você não tem permissão para cancelar este pedido");
        }

        // Só pode cancelar se está em CRIADO ou ENVIADO_ANALISE
        if (pedido.getStatus() != StatusPedido.CRIADO && pedido.getStatus() != StatusPedido.ENVIADO_ANALISE) {
            throw new IllegalArgumentException("Pedido não pode ser cancelado neste status: " + pedido.getStatus());
        }

        pedido.setStatus(StatusPedido.CANCELADO_CLIENTE);
    }

    /**
     * Aprovar pedido (apenas agente pode aprovar)
     */
    public void aprovarPedido(Long pedidoId, Cliente agenteLogado) {
        if (pedidoId == null) {
            throw new IllegalArgumentException("ID do pedido é obrigatório");
        }

        if (agenteLogado == null || !TipoUsuario.AGENTE.equals(agenteLogado.getTipoUsuario())) {
            throw new IllegalArgumentException("Apenas agentes podem aprovar pedidos");
        }

        Pedido pedido = pedidoRepository.findById(pedidoId);
        if (pedido == null) {
            throw new IllegalArgumentException("Pedido não encontrado: " + pedidoId);
        }

        if (pedido.getStatus() != StatusPedido.ENVIADO_ANALISE) {
            throw new IllegalArgumentException("Pedido deve estar em análise para ser aprovado");
        }

        pedido.setStatus(StatusPedido.APROVADO);
        pedido.setMotivoRejeicao(null);
    }

    /**
     * Rejeitar pedido (apenas agente pode rejeitar)
     */
    public void rejeitarPedido(Long pedidoId, Cliente agenteLogado, String motivo) {
        if (pedidoId == null) {
            throw new IllegalArgumentException("ID do pedido é obrigatório");
        }

        if (agenteLogado == null || !TipoUsuario.AGENTE.equals(agenteLogado.getTipoUsuario())) {
            throw new IllegalArgumentException("Apenas agentes podem rejeitar pedidos");
        }

        if (motivo == null || motivo.trim().isEmpty()) {
            throw new IllegalArgumentException("Motivo da rejeição é obrigatório");
        }

        Pedido pedido = pedidoRepository.findById(pedidoId);
        if (pedido == null) {
            throw new IllegalArgumentException("Pedido não encontrado: " + pedidoId);
        }

        if (pedido.getStatus() != StatusPedido.ENVIADO_ANALISE) {
            throw new IllegalArgumentException("Pedido deve estar em análise para ser rejeitado");
        }

        pedido.setStatus(StatusPedido.REJEITADO);
        pedido.setMotivoRejeicao(motivo);
    }

    /**
     * Enviar pedido para análise (cliente aceita termos e envia)
     */
    public void enviarParaAnalise(Long pedidoId, Cliente clienteLogado) {
        if (pedidoId == null) {
            throw new IllegalArgumentException("ID do pedido é obrigatório");
        }

        if (clienteLogado == null) {
            throw new IllegalArgumentException("Usuário não autenticado");
        }

        Pedido pedido = pedidoRepository.findById(pedidoId);
        if (pedido == null) {
            throw new IllegalArgumentException("Pedido não encontrado: " + pedidoId);
        }

        // Apenas o próprio cliente pode enviar seu pedido
        if (!pedido.getCliente().getId().equals(clienteLogado.getId())) {
            throw new IllegalArgumentException("Você não tem permissão para enviar este pedido");
        }

        if (pedido.getStatus() != StatusPedido.CRIADO) {
            throw new IllegalArgumentException("Apenas pedidos em CRIADO podem ser enviados para análise");
        }

        pedido.setStatus(StatusPedido.ENVIADO_ANALISE);
    }

    /**
     * Listar pedidos por status
     */
    @Transactional(Transactional.TxType.SUPPORTS)
    public List<Pedido> listarPorStatus(StatusPedido status) {
        return pedidoRepository.find("status = ?1 ORDER BY dataCriacao DESC", status).list();
    }
}

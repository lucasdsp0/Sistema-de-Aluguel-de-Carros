package com.pucminas.aluguelcarros.resource;

import com.pucminas.aluguelcarros.model.Automovel;
import com.pucminas.aluguelcarros.model.Cliente;
import com.pucminas.aluguelcarros.model.Pedido;
import com.pucminas.aluguelcarros.repository.AutomovelRepository;
import com.pucminas.aluguelcarros.repository.ClienteRepository;
import com.pucminas.aluguelcarros.service.AutomovelService;
import com.pucminas.aluguelcarros.service.PedidoService;
import io.quarkus.qute.Location;
import io.quarkus.qute.Template;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.CookieParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

/**
 * Resource para Pedido de Aluguel
 */
@Path("/pedido")
public class PedidoResource {

    @Inject
    PedidoService pedidoService;

    @Inject
    AutomovelService automovelService;

    @Inject
    ClienteRepository clienteRepository;

    @Inject
    AutomovelRepository automovelRepository;

    @Inject
    @Location("pedido/criar.html")
    Template criarTemplate;

    @Inject
    @Location("pedido/meus-pedidos.html")
    Template meusPedidosTemplate;

    @Inject
    @Location("pedido/detalhes.html")
    Template detalhesTemplate;

    @Inject
    @Location("pedido/validacao.html")
    Template validacaoTemplate;

    private Long extrairClienteId(String clienteIdCookie, Long clienteIdParam) {
        if (clienteIdParam != null) {
            return clienteIdParam;
        }
        if (clienteIdCookie != null && !clienteIdCookie.isEmpty()) {
            try {
                return Long.parseLong(clienteIdCookie);
            } catch (NumberFormatException e) {
                return null;
            }
        }
        return null;
    }

    /**
     * Página de criar novo pedido
     */
    @GET
    @Path("/novo")
    @Produces(MediaType.TEXT_HTML)
    public String paginaNovoPedido(@CookieParam("cliente_id") String clienteIdCookie) {
        if (clienteIdCookie == null || clienteIdCookie.isEmpty()) {
            return "<script>alert('Você precisa estar logado'); window.location.href='/auth/login';</script>";
        }

        Long clienteId = extrairClienteId(clienteIdCookie, null);
        Optional<Cliente> clienteOpt = clienteRepository.findByIdOptional(clienteId);
        String tipoUsuario = clienteOpt.isPresent() ? clienteOpt.get().getTipoUsuario().toString() : "CLIENTE";

        List<Automovel> automovels = automovelService.listarTodos();
        return criarTemplate
            .data("automovels", automovels)
            .data("erro", null)
            .data("tipoUsuario", tipoUsuario)
            .render();
    }

    /**
     * Criar novo pedido (POST)
     */
    @POST
    @Path("/criar")
    @Produces(MediaType.TEXT_HTML)
    public String criar(@CookieParam("cliente_id") String clienteIdCookie,
                       @FormParam("automovel_id") Long automovelId,
                       @FormParam("data_inicio") String dataInicioStr,
                       @FormParam("data_fim") String dataFimStr,
                       @FormParam("valor_diario") String valorDiarioStr) {
        Long clienteId = extrairClienteId(clienteIdCookie, null);
        if (clienteId == null) {
            return "<script>alert('Você precisa estar logado'); window.location.href='/auth/login';</script>";
        }

        Optional<Cliente> clienteOpt = clienteRepository.findByIdOptional(clienteId);
        if (!clienteOpt.isPresent()) {
            return "<h1>Erro: Cliente não encontrado</h1>";
        }

        try {
            LocalDate dataInicio = LocalDate.parse(dataInicioStr, DateTimeFormatter.ISO_DATE);
            LocalDate dataFim = LocalDate.parse(dataFimStr, DateTimeFormatter.ISO_DATE);
            BigDecimal valorDiario = new BigDecimal(valorDiarioStr);

            Optional<Automovel> automovelOpt = automovelRepository.findByIdOptional(automovelId);
            if (!automovelOpt.isPresent()) {
                List<Automovel> automovels = automovelService.listarTodos();
                return criarTemplate
                    .data("automovels", automovels)
                    .data("erro", "Automóvel não encontrado")
                    .render();
            }

            Pedido pedido = pedidoService.criarPedido(
                clienteOpt.get(),
                automovelOpt.get(),
                dataInicio,
                dataFim,
                valorDiario
            );

            return "<script>window.location.href='/pedido/meus-pedidos?sucesso=Pedido criado com sucesso';</script>";
        } catch (Exception e) {
            List<Automovel> automovels = automovelService.listarTodos();
            return criarTemplate
                .data("automovels", automovels)
                .data("erro", e.getMessage())
                .render();
        }
    }

    /**
     * Listar pedidos do cliente
     */
    @GET
    @Path("/meus-pedidos")
    @Produces(MediaType.TEXT_HTML)
    public String meusPedidos(@CookieParam("cliente_id") String clienteIdCookie,
                              @QueryParam("sucesso") String sucesso) {
        Long clienteId = extrairClienteId(clienteIdCookie, null);
        if (clienteId == null) {
            return "<script>alert('Você precisa estar logado'); window.location.href='/auth/login';</script>";
        }

        Optional<Cliente> clienteOpt = clienteRepository.findByIdOptional(clienteId);
        if (!clienteOpt.isPresent()) {
            return "<h1>Erro: Cliente não encontrado</h1>";
        }

        List<Pedido> pedidos = pedidoService.listarPedidosCliente(clienteOpt.get());
        String tipoUsuario = clienteOpt.get().getTipoUsuario().toString();
        return meusPedidosTemplate
            .data("pedidos", pedidos)
            .data("clienteNome", clienteOpt.get().getNome())
            .data("clienteId", clienteId)
            .data("tipoUsuario", tipoUsuario)
            .data("sucesso", sucesso)
            .render();
    }

    /**
     * Visualizar detalhes de um pedido
     */
    @GET
    @Path("/{id}")
    @Produces(MediaType.TEXT_HTML)
    public String verDetalhes(@PathParam("id") Long pedidoId,
                              @CookieParam("cliente_id") String clienteIdCookie,
                              @QueryParam("sucesso") String sucesso) {
        Long clienteId = extrairClienteId(clienteIdCookie, null);
        if (clienteId == null) {
            return "<script>alert('Você precisa estar logado'); window.location.href='/auth/login';</script>";
        }

        Optional<Pedido> pedidoOpt = pedidoService.buscarPorId(pedidoId);
        if (!pedidoOpt.isPresent()) {
            return "<h1>Pedido não encontrado</h1>";
        }

        Pedido pedido = pedidoOpt.get();

        // Verificar permissão
        Optional<Cliente> usuarioLogadoOpt = clienteRepository.findByIdOptional(clienteId);
        if (!usuarioLogadoOpt.isPresent()) {
            return "<h1>Erro: Usuário não encontrado</h1>";
        }

        return detalhesTemplate
            .data("pedido", pedido)
            .data("clienteId", clienteId)
            .data("usuarioTipo", usuarioLogadoOpt.get().getTipoUsuario())
            .data("sucesso", sucesso)
            .render();
    }

    /**
     * Cancelar pedido
     */
    @POST
    @Path("/{id}/cancelar")
    @Produces(MediaType.TEXT_HTML)
    public String cancelar(@PathParam("id") Long pedidoId,
                          @CookieParam("cliente_id") String clienteIdCookie) {
        Long clienteId = extrairClienteId(clienteIdCookie, null);
        if (clienteId == null) {
            return "<script>alert('Você precisa estar logado'); window.location.href='/auth/login';</script>";
        }

        Optional<Cliente> clienteOpt = clienteRepository.findByIdOptional(clienteId);

        try {
            pedidoService.cancelarPedido(pedidoId, clienteOpt.get());
            return "<script>window.location.href='/pedido/meus-pedidos?sucesso=Pedido cancelado com sucesso';</script>";
        } catch (Exception e) {
            return "<script>alert('" + e.getMessage() + "'); window.location.href='/pedido/meus-pedidos';</script>";
        }
    }

    /**
     * Página de validação de pedidos (apenas agentes)
     */
    @GET
    @Path("/validacao")
    @Produces(MediaType.TEXT_HTML)
    public String paginaValidacao(@CookieParam("cliente_id") String clienteIdCookie,
                                  @QueryParam("sucesso") String sucesso) {
        Long agenteId = extrairClienteId(clienteIdCookie, null);
        if (agenteId == null) {
            return "<script>alert('Você precisa estar logado'); window.location.href='/auth/login';</script>";
        }

        Optional<Cliente> usuarioOpt = clienteRepository.findByIdOptional(agenteId);
        if (!usuarioOpt.isPresent()) {
            return "<h1>Erro: Usuário não encontrado</h1>";
        }

        List<Pedido> pedidosParaValidacao = pedidoService.listarPedidosParaValidacao();
        return validacaoTemplate
            .data("pedidos", pedidosParaValidacao)
            .data("agenteId", agenteId)
            .data("sucesso", sucesso)
            .render();
    }

    /**
     * Aprovar pedido (apenas agentes)
     */
    @POST
    @Path("/{id}/aprovar")
    @Produces(MediaType.TEXT_HTML)
    public String aprovar(@PathParam("id") Long pedidoId,
                         @CookieParam("cliente_id") String clienteIdCookie) {
        Long agenteId = extrairClienteId(clienteIdCookie, null);
        if (agenteId == null) {
            return "<script>alert('Você precisa estar logado'); window.location.href='/auth/login';</script>";
        }

        Optional<Cliente> agenteOpt = clienteRepository.findByIdOptional(agenteId);

        try {
            pedidoService.aprovarPedido(pedidoId, agenteOpt.get());
            return "<script>window.location.href='/pedido/validacao?sucesso=Pedido aprovado';</script>";
        } catch (Exception e) {
            return "<script>alert('" + e.getMessage() + "'); window.location.href='/pedido/validacao';</script>";
        }
    }

    /**
     * Rejeitar pedido (apenas agentes)
     */
    @POST
    @Path("/{id}/rejeitar")
    @Produces(MediaType.TEXT_HTML)
    public String rejeitar(@PathParam("id") Long pedidoId,
                          @CookieParam("cliente_id") String clienteIdCookie,
                          @FormParam("motivo") String motivo) {
        Long agenteId = extrairClienteId(clienteIdCookie, null);
        if (agenteId == null) {
            return "<script>alert('Você precisa estar logado'); window.location.href='/auth/login';</script>";
        }

        Optional<Cliente> agenteOpt = clienteRepository.findByIdOptional(agenteId);

        try {
            pedidoService.rejeitarPedido(pedidoId, agenteOpt.get(), motivo);
            return "<script>window.location.href='/pedido/validacao?sucesso=Pedido rejeitado';</script>";
        } catch (Exception e) {
            return "<script>alert('" + e.getMessage() + "'); window.location.href='/pedido/validacao';</script>";
        }
    }

    /**
     * Enviar pedido para análise
     */
    @POST
    @Path("/{id}/enviar-analise")
    @Produces(MediaType.TEXT_HTML)
    public String enviarParaAnalise(@PathParam("id") Long pedidoId,
                                   @CookieParam("cliente_id") String clienteIdCookie) {
        Long clienteId = extrairClienteId(clienteIdCookie, null);
        if (clienteId == null) {
            return "<script>alert('Você precisa estar logado'); window.location.href='/auth/login';</script>";
        }

        Optional<Cliente> clienteOpt = clienteRepository.findByIdOptional(clienteId);

        try {
            pedidoService.enviarParaAnalise(pedidoId, clienteOpt.get());
            return "<script>window.location.href='/pedido/" + pedidoId + "?sucesso=Pedido enviado para análise';</script>";
        } catch (Exception e) {
            return "<script>alert('" + e.getMessage() + "'); window.location.href='/pedido/" + pedidoId + "';</script>";
        }
    }
}

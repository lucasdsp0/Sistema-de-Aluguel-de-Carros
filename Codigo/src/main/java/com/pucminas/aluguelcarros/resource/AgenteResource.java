package com.pucminas.aluguelcarros.resource;

import com.pucminas.aluguelcarros.model.Cliente;
import com.pucminas.aluguelcarros.service.ClienteService;
import io.quarkus.qute.Location;
import io.quarkus.qute.Template;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.util.List;

/**
 * Controller de Agente com Qute Templates
 */
@Path("/agente")
public class AgenteResource {

    @Inject
    ClienteService clienteService;

    @Inject
    @Location("agente/dashboard.html")
    Template dashboard;

    @GET
    @Path("/dashboard")
    @Produces(MediaType.TEXT_HTML)
    public String dashboardGet() {
        List<Cliente> clientes = clienteService.listarTodos();

        long clienteCount = clientes.stream().filter(c -> "CLIENTE".equals(c.getTipoUsuario().toString())).count();
        long agenteCount = clientes.stream().filter(c -> "AGENTE".equals(c.getTipoUsuario().toString())).count();

        return dashboard
                .data("clientes", clientes)
                .data("totalUsuarios", clientes.size())
                .data("totalClientes", clienteCount)
                .data("totalAgentes", agenteCount)
                .render();
    }
}

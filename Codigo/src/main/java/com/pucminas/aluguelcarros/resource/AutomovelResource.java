package com.pucminas.aluguelcarros.resource;

import com.pucminas.aluguelcarros.model.Automovel;
import com.pucminas.aluguelcarros.service.AutomovelService;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Resource para Automóvel
 */
@Path("/automovel")
@Produces(MediaType.APPLICATION_JSON)
public class AutomovelResource {

    @Inject
    AutomovelService automovelService;

    /**
     * Listar todos os automóveis
     */
    @GET
    @Path("/todos")
    public List<Automovel> listarTodos() {
        return automovelService.listarTodos();
    }

    /**
     * Listar automóveis disponíveis para um período específico
     * Query params: dataInicio, dataFim (formato: yyyy-MM-dd)
     */
    @GET
    @Path("/disponiveis")
    public List<Automovel> listarDisponiveis(@QueryParam("dataInicio") String dataInicioStr,
                                              @QueryParam("dataFim") String dataFimStr) {
        if (dataInicioStr == null || dataInicioStr.isEmpty() ||
            dataFimStr == null || dataFimStr.isEmpty()) {
            return automovelService.listarTodos();
        }

        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate dataInicio = LocalDate.parse(dataInicioStr, formatter);
            LocalDate dataFim = LocalDate.parse(dataFimStr, formatter);

            List<Automovel> todosAutomovels = automovelService.listarTodos();
            List<Automovel> disponiveis = new ArrayList<>();

            for (Automovel automovel : todosAutomovels) {
                if (automovelService.verificarDisponibilidade(automovel.getId(), dataInicio, dataFim)) {
                    disponiveis.add(automovel);
                }
            }

            return disponiveis;
        } catch (Exception e) {
            return automovelService.listarTodos();
        }
    }
}

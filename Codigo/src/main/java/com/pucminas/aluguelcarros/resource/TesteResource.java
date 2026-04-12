package com.pucminas.aluguelcarros.resource;

import com.pucminas.aluguelcarros.service.ClienteService;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

/**
 * Teste de rotas com styling profissional
 */
@Path("/teste")
public class TesteResource {

    @Inject
    ClienteService clienteService;

    @GET
    @Produces(MediaType.TEXT_HTML)
    public String teste() {
        long totalClientes = clienteService.listarTodos().size();
        return "<!DOCTYPE html>\n" +
               "<html lang=\"pt-BR\">\n" +
               "<head>\n" +
               "    <meta charset=\"UTF-8\">\n" +
               "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
               "    <title>Teste - Sistema</title>\n" +
               "    <style>\n" +
               "        * { margin: 0; padding: 0; box-sizing: border-box; }\n" +
               "        body {\n" +
               "            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;\n" +
               "            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);\n" +
               "            min-height: 100vh;\n" +
               "            display: flex;\n" +
               "            align-items: center;\n" +
               "            justify-content: center;\n" +
               "            padding: 20px;\n" +
               "        }\n" +
               "        .container {\n" +
               "            background: white;\n" +
               "            border-radius: 10px;\n" +
               "            box-shadow: 0 10px 40px rgba(0,0,0,0.3);\n" +
               "            padding: 40px;\n" +
               "            max-width: 500px;\n" +
               "            text-align: center;\n" +
               "        }\n" +
               "        h1 { color: #667eea; margin-bottom: 20px; font-size: 28px; }\n" +
               "        .success { color: #27ae60; font-weight: bold; font-size: 16px; }\n" +
               "        .stat-box {\n" +
               "            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);\n" +
               "            color: white;\n" +
               "            padding: 20px;\n" +
               "            border-radius: 8px;\n" +
               "            margin: 20px 0;\n" +
               "        }\n" +
               "        .stat-box .label { font-size: 14px; opacity: 0.9; }\n" +
               "        .stat-box .number { font-size: 32px; font-weight: bold; margin-top: 10px; }\n" +
               "        .nav-links {\n" +
               "            display: flex;\n" +
               "            gap: 10px;\n" +
               "            margin-top: 30px;\n" +
               "            justify-content: center;\n" +
               "            flex-wrap: wrap;\n" +
               "        }\n" +
               "        a {\n" +
               "            display: inline-block;\n" +
               "            padding: 10px 20px;\n" +
               "            background: #667eea;\n" +
               "            color: white;\n" +
               "            text-decoration: none;\n" +
               "            border-radius: 5px;\n" +
               "            transition: background 0.3s;\n" +
               "            font-weight: 500;\n" +
               "        }\n" +
               "        a:hover { background: #764ba2; }\n" +
               "    </style>\n" +
               "</head>\n" +
               "<body>\n" +
               "    <div class=\"container\">\n" +
               "        <h1>✅ Tudo Funcionando!</h1>\n" +
               "        <p class=\"success\">Sistema operacional com sucesso</p>\n" +
               "        \n" +
               "        <div class=\"stat-box\">\n" +
               "            <div class=\"label\">Total de Usuários Registrados</div>\n" +
               "            <div class=\"number\">" + totalClientes + "</div>\n" +
               "        </div>\n" +
               "        \n" +
               "        <div class=\"nav-links\">\n" +
               "            <a href=\"/cliente/dashboard\">📊 Cliente Dashboard</a>\n" +
               "            <a href=\"/agente/dashboard\">🏢 Agente Dashboard</a>\n" +
               "            <a href=\"/\">🏠 Início</a>\n" +
               "        </div>\n" +
               "    </div>\n" +
               "</body>\n" +
               "</html>";
    }

    @GET
    @Path("/clientes")
    @Produces(MediaType.APPLICATION_JSON)
    public String clientes() {
        return clienteService.listarTodos().toString();
    }
}

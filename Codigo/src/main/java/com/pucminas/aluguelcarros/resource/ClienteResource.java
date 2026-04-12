package com.pucminas.aluguelcarros.resource;

import com.pucminas.aluguelcarros.model.Cliente;
import com.pucminas.aluguelcarros.service.ClienteService;
import io.quarkus.qute.Location;
import io.quarkus.qute.Template;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.util.List;

/**
 * Controller de Cliente com Qute Templates
 */
@Path("/cliente")
public class ClienteResource {

    @Inject
    ClienteService clienteService;

    @Inject
    @Location("cliente/dashboard.html")
    Template dashboard;

    @Inject
    @Location("cliente/listar.html")
    Template listar;

    @GET
    @Path("/dashboard")
    @Produces(MediaType.TEXT_HTML)
    public String dashboardGet() {
        List<Cliente> clientes = clienteService.listarTodos();
        return dashboard.data("totalClientes", clientes.size()).render();
    }

    @GET
    @Path("/listar")
    @Produces(MediaType.TEXT_HTML)
    public String listarGet() {
        List<Cliente> clientes = clienteService.listarTodos();
        return listar
                .data("clientes", clientes)
                .data("totalClientes", clientes.size())
                .render();
    }

    @GET
    @Path("/perfil")
    @Produces(MediaType.TEXT_HTML)
    public String perfil() {
        return "<!DOCTYPE html>\n" +
               "<html lang=\"pt-BR\">\n" +
               "<head>\n" +
               "    <meta charset=\"UTF-8\">\n" +
               "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
               "    <title>Meu Perfil</title>\n" +
               "    <style>\n" +
               "        * { margin: 0; padding: 0; box-sizing: border-box; }\n" +
               "        body {\n" +
               "            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;\n" +
               "            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);\n" +
               "            min-height: 100vh;\n" +
               "            padding: 30px 20px;\n" +
               "        }\n" +
               "        .container {\n" +
               "            max-width: 1000px;\n" +
               "            margin: 0 auto;\n" +
               "            background: white;\n" +
               "            border-radius: 10px;\n" +
               "            box-shadow: 0 10px 40px rgba(0,0,0,0.3);\n" +
               "            padding: 40px;\n" +
               "        }\n" +
               "        h1 { color: #333; margin-bottom: 30px; border-bottom: 3px solid #667eea; padding-bottom: 15px; }\n" +
               "        .profile-info {\n" +
               "            background: #f9f9f9;\n" +
               "            padding: 20px;\n" +
               "            border-radius: 5px;\n" +
               "            margin: 20px 0;\n" +
               "            border-left: 4px solid #667eea;\n" +
               "        }\n" +
               "        .profile-info p { margin: 10px 0; color: #555; }\n" +
               "        .profile-info strong { color: #333; }\n" +
               "        .nav-links {\n" +
               "            display: flex;\n" +
               "            gap: 10px;\n" +
               "            margin-top: 30px;\n" +
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
               "        <h1>👤 Meu Perfil</h1>\n" +
               "        <div class=\"profile-info\">\n" +
               "            <p><strong>Email:</strong> joao@email.com</p>\n" +
               "            <p><strong>Nome:</strong> João Silva</p>\n" +
               "            <p><strong>Tipo de Usuário:</strong> Cliente</p>\n" +
               "            <p><strong>Status:</strong> Ativo</p>\n" +
               "        </div>\n" +
               "        <div class=\"nav-links\">\n" +
               "            <a href=\"/cliente/editar/1\">✏️ Editar Dados</a>\n" +
               "            <a href=\"/cliente/dashboard\">📊 Voltar ao Dashboard</a>\n" +
               "            <a href=\"/\">🏠 Voltar</a>\n" +
               "        </div>\n" +
               "    </div>\n" +
               "</body>\n" +
               "</html>";
    }

    @GET
    @Path("/editar/{id}")
    @Produces(MediaType.TEXT_HTML)
    public String editar(@PathParam("id") Long id) {
        return "<!DOCTYPE html>\n" +
               "<html lang=\"pt-BR\">\n" +
               "<head>\n" +
               "    <meta charset=\"UTF-8\">\n" +
               "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
               "    <title>Editar Dados</title>\n" +
               "    <style>\n" +
               "        * { margin: 0; padding: 0; box-sizing: border-box; }\n" +
               "        body {\n" +
               "            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;\n" +
               "            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);\n" +
               "            min-height: 100vh;\n" +
               "            padding: 30px 20px;\n" +
               "        }\n" +
               "        .container {\n" +
               "            max-width: 1000px;\n" +
               "            margin: 0 auto;\n" +
               "            background: white;\n" +
               "            border-radius: 10px;\n" +
               "            box-shadow: 0 10px 40px rgba(0,0,0,0.3);\n" +
               "            padding: 40px;\n" +
               "        }\n" +
               "        h1 { color: #333; margin-bottom: 30px; border-bottom: 3px solid #667eea; padding-bottom: 15px; }\n" +
               "        .form-group { margin-bottom: 20px; }\n" +
               "        label { display: block; margin-bottom: 8px; color: #555; font-weight: 600; }\n" +
               "        input {\n" +
               "            width: 100%;\n" +
               "            padding: 10px 15px;\n" +
               "            border: 1px solid #ddd;\n" +
               "            border-radius: 5px;\n" +
               "            font-size: 14px;\n" +
               "        }\n" +
               "        input:focus { outline: none; border-color: #667eea; }\n" +
               "        button {\n" +
               "            background: #667eea;\n" +
               "            color: white;\n" +
               "            padding: 12px 30px;\n" +
               "            border: none;\n" +
               "            border-radius: 5px;\n" +
               "            cursor: pointer;\n" +
               "            font-weight: 600;\n" +
               "            transition: background 0.3s;\n" +
               "        }\n" +
               "        button:hover { background: #764ba2; }\n" +
               "        .nav-links {\n" +
               "            display: flex;\n" +
               "            gap: 10px;\n" +
               "            margin-top: 30px;\n" +
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
               "        <h1>✏️ Editar Dados do Cliente</h1>\n" +
               "        <form>\n" +
               "            <div class=\"form-group\">\n" +
               "                <label for=\"nome\">Nome:</label>\n" +
               "                <input type=\"text\" id=\"nome\" name=\"nome\" value=\"João Silva\" required>\n" +
               "            </div>\n" +
               "            <div class=\"form-group\">\n" +
               "                <label for=\"email\">Email:</label>\n" +
               "                <input type=\"email\" id=\"email\" name=\"email\" value=\"joao@email.com\" required>\n" +
               "            </div>\n" +
               "            <div class=\"form-group\">\n" +
               "                <label for=\"cpf\">CPF:</label>\n" +
               "                <input type=\"text\" id=\"cpf\" name=\"cpf\" value=\"123.456.789-00\" required>\n" +
               "            </div>\n" +
               "            <button type=\"submit\">💾 Salvar Alterações</button>\n" +
               "        </form>\n" +
               "        <div class=\"nav-links\" style=\"margin-top: 30px;\">\n" +
               "            <a href=\"/cliente/perfil\">👤 Voltar ao Perfil</a>\n" +
               "            <a href=\"/cliente/dashboard\">📊 Dashboard</a>\n" +
               "        </div>\n" +
               "    </div>\n" +
               "</body>\n" +
               "</html>";
    }
}

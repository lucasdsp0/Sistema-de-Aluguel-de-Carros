package com.pucminas.aluguelcarros.resource;

import com.pucminas.aluguelcarros.model.Cliente;
import com.pucminas.aluguelcarros.model.TipoUsuario;
import com.pucminas.aluguelcarros.service.ClienteService;
import io.quarkus.qute.Location;
import io.quarkus.qute.Template;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.NewCookie;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Controller de Autenticação com Qute Templates
 */
@Path("")
public class AuthResource {

    @Inject
    ClienteService clienteService;

    @Inject
    @Location("auth/home.html")
    Template home;

    @Inject
    @Location("auth/login.html")
    Template login;

    @Inject
    @Location("auth/cadastro.html")
    Template cadastro;

    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @GET
    @Produces(MediaType.TEXT_HTML)
    public String home() {
        return home.instance().render();
    }

    @GET
    @Path("auth/login")
    @Produces(MediaType.TEXT_HTML)
    public String loginGet(@QueryParam("cadastro") String cadastroStatus) {
        String mensagem = null;
        if ("sucesso".equals(cadastroStatus)) {
            mensagem = "✅ Cadastro realizado com sucesso! Faça login com suas credenciais.";
        }
        return login.data("mensagem", mensagem).render();
    }

    @GET
    @Path("auth/cadastro")
    @Produces(MediaType.TEXT_HTML)
    public String cadastroGet() {
        return cadastro.instance().render();
    }

    @POST
    @Path("auth/login")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response loginPost(@FormParam("email") String email, @FormParam("password") String senha) throws java.net.URISyntaxException {
        if (email == null || email.isEmpty() || senha == null || senha.isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Email e senha são obrigatórios").build();
        }

        var clienteOpt = clienteService.buscarPorEmail(email);
        if (clienteOpt.isEmpty()) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("Email ou senha incorretos").build();
        }

        Cliente cliente = clienteOpt.get();
        if (!passwordEncoder.matches(senha, cliente.getSenha())) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("Email ou senha incorretos").build();
        }

        // Login success - redirect to dashboard
        if ("CLIENTE".equals(cliente.getTipoUsuario().toString())) {
            return Response.seeOther(new java.net.URI("http://localhost:8080/cliente/dashboard"))
                    .cookie(new NewCookie("cliente_id", cliente.getId().toString(), "/", null, null, -1, false, true))
                    .cookie(new NewCookie("tipo_usuario", cliente.getTipoUsuario().toString(), "/", null, null, -1, false, true))
                    .build();
        } else {
            return Response.seeOther(new java.net.URI("http://localhost:8080/agente/dashboard"))
                    .cookie(new NewCookie("cliente_id", cliente.getId().toString(), "/", null, null, -1, false, true))
                    .cookie(new NewCookie("tipo_usuario", cliente.getTipoUsuario().toString(), "/", null, null, -1, false, true))
                    .build();
        }
    }

    @POST
    @Path("auth/cadastro")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response cadastroPost(@FormParam("nome") String nome,
            @FormParam("email") String email,
            @FormParam("cpf") String cpf,
            @FormParam("senha") String senha) throws java.net.URISyntaxException {

        if (nome == null || nome.isEmpty() || email == null || email.isEmpty() ||
                cpf == null || cpf.isEmpty() || senha == null || senha.isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Todos os campos são obrigatórios").build();
        }

        try {
            Cliente novoCliente = new Cliente();
            novoCliente.setNome(nome);
            novoCliente.setEmail(email);
            novoCliente.setCpf(cpf);
            novoCliente.setSenha(senha);
            novoCliente.setTipoUsuario(TipoUsuario.CLIENTE);

            clienteService.cadastrar(novoCliente);

            return Response.seeOther(new java.net.URI("http://localhost:8080/auth/login?cadastro=sucesso"))
                    .build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Erro ao cadastrar: " + e.getMessage()).build();
        }
    }
}

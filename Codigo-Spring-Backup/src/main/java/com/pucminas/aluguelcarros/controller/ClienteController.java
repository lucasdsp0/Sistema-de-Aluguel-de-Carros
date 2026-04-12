package com.pucminas.aluguelcarros.controller;

import com.pucminas.aluguelcarros.model.Cliente;
import com.pucminas.aluguelcarros.model.EntidadeEmpregadora;
import com.pucminas.aluguelcarros.service.ClienteService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/cliente")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    // -------------------------------------------------------
    // Dashboard do Cliente
    // -------------------------------------------------------
    @GetMapping("/dashboard")
    public String dashboard(Authentication auth, Model model) {
        clienteService.buscarPorEmail(auth.getName()).ifPresent(c ->
                model.addAttribute("cliente", c));
        return "cliente/dashboard";
    }

    // -------------------------------------------------------
    // Listar todos os clientes (visão administrativa / agente)
    // -------------------------------------------------------
    @GetMapping("/listar")
    public String listar(Model model) {
        model.addAttribute("clientes", clienteService.listarTodos());
        return "cliente/listar";
    }

    // -------------------------------------------------------
    // Visualizar perfil do próprio cliente
    // -------------------------------------------------------
    @GetMapping("/perfil")
    public String perfil(Authentication auth, Model model) {
        Optional<Cliente> clienteOpt = clienteService.buscarPorEmail(auth.getName());
        if (clienteOpt.isEmpty()) {
            return "redirect:/login";
        }
        model.addAttribute("cliente", clienteOpt.get());
        return "cliente/perfil";
    }

    // -------------------------------------------------------
    // Editar perfil do próprio cliente
    // -------------------------------------------------------
    @GetMapping("/editar")
    public String editarForm(Authentication auth, Model model) {
        Optional<Cliente> clienteOpt = clienteService.buscarPorEmail(auth.getName());
        if (clienteOpt.isEmpty()) {
            return "redirect:/login";
        }
        Cliente cliente = clienteOpt.get();
        // Garantir pelo menos 3 slots no formulário
        while (cliente.getEntidadesEmpregadoras().size() < 3) {
            cliente.getEntidadesEmpregadoras().add(new EntidadeEmpregadora());
        }
        model.addAttribute("cliente", cliente);
        return "cliente/editar";
    }

    @PostMapping("/editar")
    public String editar(
            Authentication auth,
            @Valid @ModelAttribute("cliente") Cliente clienteForm,
            BindingResult result,
            @RequestParam(value = "nomeEntidade", required = false) List<String> nomesEntidade,
            @RequestParam(value = "rendimento", required = false) List<BigDecimal> rendimentos,
            Model model,
            RedirectAttributes redirectAttributes) {

        Optional<Cliente> clienteOpt = clienteService.buscarPorEmail(auth.getName());
        if (clienteOpt.isEmpty()) {
            return "redirect:/login";
        }

        // Reconstruir entidades empregadoras
        List<EntidadeEmpregadora> entidades = new ArrayList<>();
        if (nomesEntidade != null) {
            for (int i = 0; i < nomesEntidade.size(); i++) {
                String nome = nomesEntidade.get(i);
                BigDecimal rendimento = (rendimentos != null && i < rendimentos.size()) ? rendimentos.get(i) : null;
                if (nome != null && !nome.isBlank() && rendimento != null) {
                    EntidadeEmpregadora e = new EntidadeEmpregadora();
                    e.setNomeEntidade(nome.trim());
                    e.setRendimento(rendimento);
                    entidades.add(e);
                }
            }
        }
        clienteForm.getEntidadesEmpregadoras().clear();
        clienteForm.getEntidadesEmpregadoras().addAll(entidades);

        if (result.hasErrors()) {
            return "cliente/editar";
        }

        try {
            clienteService.atualizar(clienteOpt.get().getId(), clienteForm);
            redirectAttributes.addFlashAttribute("mensagem", "Perfil atualizado com sucesso!");
            return "redirect:/cliente/perfil";
        } catch (IllegalArgumentException e) {
            model.addAttribute("erro", e.getMessage());
            return "cliente/editar";
        }
    }

    // -------------------------------------------------------
    // Excluir conta (soft - apenas para demonstração do CRUD)
    // -------------------------------------------------------
    @PostMapping("/excluir/{id}")
    public String excluir(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            clienteService.excluir(id);
            redirectAttributes.addFlashAttribute("mensagem", "Cliente excluído com sucesso.");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("erro", e.getMessage());
        }
        return "redirect:/cliente/listar";
    }
}

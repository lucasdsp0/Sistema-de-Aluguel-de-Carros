package com.pucminas.aluguelcarros.controller;

import com.pucminas.aluguelcarros.model.Cliente;
import com.pucminas.aluguelcarros.model.EntidadeEmpregadora;
import com.pucminas.aluguelcarros.model.TipoUsuario;
import com.pucminas.aluguelcarros.service.ClienteService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Controller
public class AuthController {

    @Autowired
    private ClienteService clienteService;

    // -------------------------------------------------------
    // Página inicial
    // -------------------------------------------------------
    @GetMapping("/")
    public String index() {
        return "redirect:/login";
    }

    // -------------------------------------------------------
    // Login
    // -------------------------------------------------------
    @GetMapping("/login")
    public String loginPage(@RequestParam(required = false) String erro,
                            @RequestParam(required = false) String logout,
                            Model model) {
        if (erro != null) {
            model.addAttribute("erro", "E-mail ou senha inválidos.");
        }
        if (logout != null) {
            model.addAttribute("mensagem", "Você saiu do sistema com sucesso.");
        }
        return "auth/login";
    }

    // -------------------------------------------------------
    // Dashboard — redireciona conforme perfil
    // -------------------------------------------------------
    @GetMapping("/dashboard")
    public String dashboard(Authentication auth) {
        if (auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_CLIENTE"))) {
            return "redirect:/cliente/dashboard";
        } else if (auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_AGENTE"))) {
            return "redirect:/agente/dashboard";
        }
        return "redirect:/login";
    }

    // -------------------------------------------------------
    // Cadastro de novo cliente (US01)
    // -------------------------------------------------------
    @GetMapping("/cadastro")
    public String cadastroForm(Model model) {
        Cliente cliente = new Cliente();
        // Pré-popular com 3 slots vazios para o formulário
        for (int i = 0; i < 3; i++) {
            cliente.getEntidadesEmpregadoras().add(new EntidadeEmpregadora());
        }
        model.addAttribute("cliente", cliente);
        return "auth/cadastro";
    }

    @PostMapping("/cadastro")
    public String cadastrar(
            @Valid @ModelAttribute("cliente") Cliente cliente,
            BindingResult result,
            @RequestParam(value = "nomeEntidade", required = false) List<String> nomesEntidade,
            @RequestParam(value = "rendimento", required = false) List<BigDecimal> rendimentos,
            Model model,
            RedirectAttributes redirectAttributes) {

        // Reconstruir entidades empregadoras a partir dos parâmetros individuais
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
        cliente.getEntidadesEmpregadoras().clear();
        cliente.getEntidadesEmpregadoras().addAll(entidades);

        if (result.hasErrors()) {
            return "auth/cadastro";
        }

        try {
            cliente.setTipoUsuario(TipoUsuario.CLIENTE);
            clienteService.cadastrar(cliente);
            redirectAttributes.addFlashAttribute("mensagem", "Cadastro realizado com sucesso! Faça login.");
            return "redirect:/login";
        } catch (IllegalArgumentException e) {
            model.addAttribute("erro", e.getMessage());
            return "auth/cadastro";
        }
    }
}

package com.pucminas.aluguelcarros.controller;

import com.pucminas.aluguelcarros.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/agente")
public class AgenteController {

    @Autowired
    private ClienteService clienteService;

    @GetMapping("/dashboard")
    public String dashboard(Authentication auth, Model model) {
        clienteService.buscarPorEmail(auth.getName()).ifPresent(a ->
                model.addAttribute("agente", a));
        model.addAttribute("clientes", clienteService.listarTodos());
        return "agente/dashboard";
    }
}

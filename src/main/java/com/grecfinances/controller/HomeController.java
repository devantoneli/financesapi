package com.grecfinances.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import com.grecfinances.model.UsuarioModel;
import com.grecfinances.model.ResumoFinanceiroServiceModel;

@Controller
public class HomeController {
    @GetMapping("/home")
    public String home(@SessionAttribute(name = "usuarioLogado", required = false) UsuarioModel usuario, Model model) {
        String mensagem = "Olá, " + (usuario != null ? usuario.getNome() : "variável do Java") + "!";
        model.addAttribute("mensagem", mensagem);
        model.addAttribute("nomeUsuario", usuario.getNome());
        return "home";
    }

    @GetMapping("/")
    public String root() {
        return "redirect:/home";
    }
}

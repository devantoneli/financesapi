package com.grecfinances.controller;

import com.grecfinances.repository.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import com.grecfinances.model.UsuarioModel;

@Controller
public class HomeController {

    @Autowired
    private CategoriaRepository categoriaRepository;

    @GetMapping("/home")
    public String home(@SessionAttribute(name = "usuarioLogado", required = false) UsuarioModel usuario, Model model) {
        String mensagem = "Olá, " + (usuario != null ? usuario.getNome() : "visitante") + "!";
        model.addAttribute("mensagem", mensagem);
        model.addAttribute("nomeUsuario", usuario.getNome());
        model.addAttribute("categorias", categoriaRepository.findAll());
        return "home";
    }

    @GetMapping("/")
    public String root() {
        return "redirect:/home";
    }
}

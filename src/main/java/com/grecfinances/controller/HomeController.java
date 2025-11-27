package com.grecfinances.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import com.grecfinances.model.UsuarioModel;
import com.grecfinances.model.ResumoFinanceiroServiceModel;

@Controller
public class HomeController {

    @GetMapping("/dashboard")
    public String dashboard(@SessionAttribute(name = "usuarioLogado", required = false) UsuarioModel usuario, Model model) {
        ResumoFinanceiroServiceModel resumo = new ResumoFinanceiroServiceModel();
        
        if (usuario != null) {
            resumo.setNomeUsuario(usuario.getNome());
        }
        
        model.addAttribute("resumo", resumo);
        return "dashboard";
    }

    @GetMapping("/home")
    public String home(@SessionAttribute(name = "usuarioLogado", required = false) UsuarioModel usuario, Model model) {
        String mensagem = "Olá, " + (usuario != null ? usuario.getNome() : "variável do Java") + "!";
        model.addAttribute("mensagem", mensagem);
        return "home";
    }

    @GetMapping("/")
    public String root() {
        return "redirect:/dashboard";
    }
}

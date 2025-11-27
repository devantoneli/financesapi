package com.grecfinances.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import com.grecfinances.model.ResumoFinanceiroServiceModel;
import com.grecfinances.model.UsuarioModel;

@Controller
public class DashboardController {

    @GetMapping("/dashboard")
    public String dashboard(@SessionAttribute(name = "usuarioLogado", required = false) UsuarioModel usuario, Model model) {
        ResumoFinanceiroServiceModel resumo = new ResumoFinanceiroServiceModel();
        
        if (usuario != null) {
            resumo.setNomeUsuario(usuario.getNome());
        }
        
        model.addAttribute("resumo", resumo);
        return "dashboard";
    }
}

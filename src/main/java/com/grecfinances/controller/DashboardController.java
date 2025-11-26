package com.grecfinances.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;

import com.grecfinances.model.ResumoFinanceiroServiceModel;
// imports removed (controller no longer uses sample numeric data)

@Controller
public class DashboardController {

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        // Sem dados de exemplo aqui — a view está pronta para receber o modelo
        ResumoFinanceiroServiceModel resumo = new ResumoFinanceiroServiceModel();
        // não popular campos: placeholders serão exibidos na view
        model.addAttribute("resumo", resumo);
        return "dashboard";
    }
}

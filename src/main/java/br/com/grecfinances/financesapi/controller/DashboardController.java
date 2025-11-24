package br.com.grecfinances.financesapi.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class DashboardController {
    @GetMapping("/")
    public String home(Model model){
        model.addAttribute("mensagem", "Bem-vindo ao controle financeiro!");
        return "home"; // retorna um arquivo home.html
    }

}

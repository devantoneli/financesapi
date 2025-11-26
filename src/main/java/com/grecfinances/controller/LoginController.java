package com.grecfinances.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {
    @GetMapping("/hello")
    public String hello(Model model) {
        String mensagem = "Olá, variável do Java!";
        model.addAttribute("mensagem", mensagem);
        return "hello";
    }

    @GetMapping({"/", "/login"})
    public String login() {
        return "login";
    }
}

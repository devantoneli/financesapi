package com.grecfinances.controller;

import com.grecfinances.model.UsuarioModel;
import com.grecfinances.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
public class LoginController {

    @Autowired
    private UsuarioRepository usuarioRepository;

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

    @PostMapping("/login")
    public String doLogin(@RequestParam("username") String username,
                          @RequestParam("password") String password,
                          HttpSession session,
                          RedirectAttributes redirectAttributes) {

        Optional<UsuarioModel> usuarioOpt = usuarioRepository.findByEmail(username);
        if (usuarioOpt.isPresent()) {
            UsuarioModel usuario = usuarioOpt.get();
            if (usuario.getSenha() != null && usuario.getSenha().equals(password)) {
                // Autenticado com sucesso
                session.setAttribute("usuarioLogado", usuario);
                return "redirect:/hello"; // página pós-login
            }
        }

        // Falha ao autenticar
        redirectAttributes.addFlashAttribute("loginError", "Email ou senha inválidos");
        return "redirect:/login";
    }
}

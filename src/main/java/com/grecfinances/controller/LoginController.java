package com.grecfinances.controller;

import com.grecfinances.model.UsuarioModel;
import com.grecfinances.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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

    @GetMapping("/login")
    public String loginPage() {
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
                session.setAttribute("usuarioLogado", usuario);
                return "redirect:/home";
            }
        }

        redirectAttributes.addFlashAttribute("loginError", "Email ou senha inválidos");
        return "redirect:/login";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}

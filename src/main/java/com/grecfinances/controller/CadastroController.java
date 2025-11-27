package com.grecfinances.controller;

import com.grecfinances.model.UsuarioModel;
import com.grecfinances.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class CadastroController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @GetMapping("/cadastro")
    public String exibirFormularioCadastro(Model model) {
        model.addAttribute("usuario", new UsuarioModel());
        return "cadastro";
    }

    @PostMapping("/cadastro")
    public String processarCadastro(UsuarioModel usuario) {
        if (usuario != null) {
            usuarioRepository.save(usuario);
        }
        return "redirect:/login";
    }
}

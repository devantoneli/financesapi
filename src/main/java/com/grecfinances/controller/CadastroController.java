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
    public String processarCadastro(UsuarioModel usuario, Model model) {
        if (usuario == null || usuario.getEmail() == null || usuario.getSenha() == null || usuario.getNome() == null ||
            usuario.getEmail().trim().isEmpty() || usuario.getSenha().isEmpty() || usuario.getNome().trim().isEmpty()) {
            model.addAttribute("cadastroError", "Todos os campos são obrigatórios.");
            model.addAttribute("usuario", usuario);
            return "cadastro";
        }

        // 1. Verificação de e-mail duplicado
        if (usuarioRepository.findByEmail(usuario.getEmail()).isPresent()) {
            model.addAttribute("cadastroError", "Este e-mail já está em uso. Tente outro.");
            model.addAttribute("usuario", usuario);
            return "cadastro";
        }

        // 2. Validação da política de senha
        String senha = usuario.getSenha();
        String specialCharacters = "!@#$%&_";
        boolean hasLetter = senha.chars().anyMatch(Character::isLetter);
        boolean hasDigit = senha.chars().anyMatch(Character::isDigit);
        boolean hasSpecialChar = senha.chars().anyMatch(c -> specialCharacters.indexOf(c) != -1);

        if (senha.length() < 6 || !hasLetter || !hasDigit || !hasSpecialChar) {
            model.addAttribute("cadastroError", "A senha deve ter no mínimo 6 caracteres, contendo pelo menos uma letra, um número e um caractere especial entre '!@#$%&_'.");
            model.addAttribute("usuario", usuario);
            return "cadastro";
        }
        
        usuarioRepository.save(usuario);
        return "redirect:/login?cadastro=sucesso";
    }
}

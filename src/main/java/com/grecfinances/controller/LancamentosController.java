package com.grecfinances.controller;

import com.grecfinances.model.LancamentoModel;
import com.grecfinances.model.UsuarioModel;
import com.grecfinances.repository.LancamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import java.util.List;

@Controller
public class LancamentosController {

    @Autowired
    private LancamentoRepository lancamentoRepository;

    @GetMapping("/lancamentos")
    public String lancamentos(Model model, @SessionAttribute(name = "usuarioLogado") UsuarioModel usuario) {
        List<LancamentoModel> lancamentos = lancamentoRepository.findByUsuario(usuario);
        
        model.addAttribute("lancamentos", lancamentos);
        model.addAttribute("total", lancamentos.size());
        return "lancamentos";
    }
}

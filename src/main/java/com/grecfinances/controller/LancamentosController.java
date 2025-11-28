package com.grecfinances.controller;

import com.grecfinances.model.LancamentoModel;
import com.grecfinances.model.UsuarioModel;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;

@Controller
public class LancamentosController {
    

    @GetMapping("/lancamentos")
    public String lancamentos(Model model, @SessionAttribute(name = "usuarioLogado") UsuarioModel usuario) {
        // Dados de exemplo (mock) para visualização
        List<LancamentoModel> lancamentos = new ArrayList<>();
        Long usuarioId = usuario.getId();
        LancamentoModel t1 = new LancamentoModel();
        
        t1.setId(1L);
        t1.setUsuario(usuarioId);
        t1.setDescricao("Freelancer - Projeto Web");
        t1.setCategoria(1);
        t1.setValor(new BigDecimal("1500.00"));
        t1.setData(LocalDate.of(2025, 11, 25));
        t1.setDescricao("Uber para Casa");
        t1.setCategoria(2);
        t1.setValor(new BigDecimal("10.00"));
        t1.setData(LocalDate.of(2025, 11, 24));
        t1.setTipo("Receita");
        lancamentos.add(t1);

        model.addAttribute("lancamentos", lancamentos);
        model.addAttribute("total", lancamentos.size());
        return "lancamentos";
    }
}

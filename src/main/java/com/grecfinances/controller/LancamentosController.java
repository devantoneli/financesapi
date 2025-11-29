package com.grecfinances.controller;

import com.grecfinances.model.LancamentoModel;
import com.grecfinances.model.UsuarioModel;
import com.grecfinances.model.CategoriaModel;

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

        // Categoria Mock 1
        CategoriaModel cat1 = new CategoriaModel();
        cat1.setId(1);
        cat1.setNome("Trabalho");

        // Categoria Mock 2
        CategoriaModel cat2 = new CategoriaModel();
        cat2.setId(2);
        cat2.setNome("Transporte");

        LancamentoModel t1 = new LancamentoModel();
        t1.setId(1L);
        t1.setUsuario(usuario);
        t1.setDescricao("Freelancer - Projeto Web");
        t1.setCategoria(cat1);
        t1.setValor(new BigDecimal("1500.00"));
        t1.setData(LocalDate.of(2025, 11, 25));
        t1.setTipo("Receita");
        lancamentos.add(t1);

        LancamentoModel t2 = new LancamentoModel();
        t2.setId(2L);
        t2.setUsuario(usuario);
        t2.setDescricao("Uber para Casa");
        t2.setCategoria(cat2);
        t2.setValor(new BigDecimal("10.00"));
        t2.setData(LocalDate.of(2025, 11, 24));
        t2.setTipo("Despesa");
        lancamentos.add(t2);

        model.addAttribute("lancamentos", lancamentos);
        model.addAttribute("total", lancamentos.size());
        return "lancamentos";
    }
}

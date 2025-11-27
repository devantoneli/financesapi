package com.grecfinances.controller;

import com.grecfinances.model.TransacaoModel;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;

@Controller
public class LancamentosController {

    @GetMapping("/lancamentos")
    public String lancamentos(Model model) {
        // Dados de exemplo (mock) para visualização
        List<TransacaoModel> lancamentos = new ArrayList<>();
        
        TransacaoModel t1 = new TransacaoModel();
        t1.setId(1L);
        t1.setDescricao("Freelancer - Projeto Web");
        t1.setCategoria("Trabalho");
        t1.setValor(new BigDecimal("1500.00"));
        t1.setData(LocalDate.of(2025, 11, 25));
        t1.setTipo("Receita");
        lancamentos.add(t1);

        TransacaoModel t2 = new TransacaoModel();
        t2.setId(2L);
        t2.setDescricao("Aluguel - Apartamento");
        t2.setCategoria("Moradia");
        t2.setValor(new BigDecimal("1200.00"));
        t2.setData(LocalDate.of(2025, 11, 24));
        t2.setTipo("Despesa");
        lancamentos.add(t2);

        TransacaoModel t3 = new TransacaoModel();
        t3.setId(3L);
        t3.setDescricao("Supermercado - Alimentos");
        t3.setCategoria("Alimentação");
        t3.setValor(new BigDecimal("320.50"));
        t3.setData(LocalDate.of(2025, 11, 24));
        t3.setTipo("Despesa");
        lancamentos.add(t3);

        TransacaoModel t4 = new TransacaoModel();
        t4.setId(4L);
        t4.setDescricao("Salário");
        t4.setCategoria("Renda");
        t4.setValor(new BigDecimal("5000.00"));
        t4.setData(LocalDate.of(2025, 11, 23));
        t4.setTipo("Receita");
        lancamentos.add(t4);

        TransacaoModel t5 = new TransacaoModel();
        t5.setId(5L);
        t5.setDescricao("Academia - Mensalidade");
        t5.setCategoria("Saúde");
        t5.setValor(new BigDecimal("150.00"));
        t5.setData(LocalDate.of(2025, 11, 22));
        t5.setTipo("Despesa");
        lancamentos.add(t5);

        model.addAttribute("lancamentos", lancamentos);
        model.addAttribute("total", lancamentos.size());
        return "lancamentos";
    }
}

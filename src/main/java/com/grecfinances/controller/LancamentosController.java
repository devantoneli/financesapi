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
        t1.setDescricao("Uber para Casa");
        t1.setCategoria("Dia a Dia");
        t1.setValor(10.00f);
        t1.setData(LocalDate.of(2025, 11, 24));
        t1.setTipo("Receita");
        lancamentos.add(t1);

        TransacaoModel t2 = new TransacaoModel();
        t2.setId(2L);
        t2.setDescricao("Uber para Casa");
        t2.setCategoria("Dia a Dia");
        t2.setValor(10.00f);
        t2.setData(LocalDate.of(2025, 11, 24));
        t2.setTipo("Despesa");
        lancamentos.add(t2);

        TransacaoModel t3 = new TransacaoModel();
        t3.setId(3L);
        t3.setDescricao("Uber para Casa");
        t3.setCategoria("Dia a Dia");
        t3.setValor(10.00f);
        t3.setData(LocalDate.of(2025, 11, 24));
        t3.setTipo("Receita");
        lancamentos.add(t3);

        TransacaoModel t4 = new TransacaoModel();
        t4.setId(4L);
        t4.setDescricao("Uber para Casa");
        t4.setCategoria("Dia a Dia");
        t4.setValor(10.00f);
        t4.setData(LocalDate.of(2025, 11, 24));
        t4.setTipo("Receita");
        lancamentos.add(t4);

        TransacaoModel t5 = new TransacaoModel();
        t5.setId(5L);
        t5.setDescricao("Uber para Casa");
        t5.setCategoria("Dia a Dia");
        t5.setValor(10.00f);
        t5.setData(LocalDate.of(2025, 11, 24));
        t5.setTipo("Receita");
        lancamentos.add(t5);

        TransacaoModel t6 = new TransacaoModel();
        t6.setId(6L);
        t6.setDescricao("Uber para Casa");
        t6.setCategoria("Dia a Dia");
        t6.setValor(10.00f);
        t6.setData(LocalDate.of(2025, 11, 24));
        t6.setTipo("Receita");
        lancamentos.add(t6);

        TransacaoModel t7 = new TransacaoModel();
        t7.setId(7L);
        t7.setDescricao("Uber para Casa");
        t7.setCategoria("Dia a Dia");
        t7.setValor(10.00f);
        t7.setData(LocalDate.of(2025, 11, 24));
        t7.setTipo("Receita");
        lancamentos.add(t7);

        model.addAttribute("lancamentos", lancamentos);
        model.addAttribute("total", 150);
        return "lancamentos";
    }
}

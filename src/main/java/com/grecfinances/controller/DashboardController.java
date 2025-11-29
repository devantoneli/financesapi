package com.grecfinances.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import com.grecfinances.model.CategoriaDespesa;
import com.grecfinances.model.LancamentoModel;
import com.grecfinances.model.ResumoFinanceiroServiceModel;
import com.grecfinances.model.UsuarioModel;
import com.grecfinances.repository.LancamentoRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class DashboardController {

    @Autowired
    private LancamentoRepository lancamentoRepository;

    @GetMapping("/dashboard")
    public String dashboard(@SessionAttribute(name = "usuarioLogado", required = false) UsuarioModel usuario, Model model) {
        if (usuario == null) {
            return "redirect:/login";
        }

        ResumoFinanceiroServiceModel resumo = new ResumoFinanceiroServiceModel();
        resumo.setNomeUsuario(usuario.getNome());

        List<LancamentoModel> todosLancamentos = lancamentoRepository.findByUsuario(usuario);
        
        LocalDate hoje = LocalDate.now();
        LocalDate inicioDoMes = hoje.with(TemporalAdjusters.firstDayOfMonth());
        LocalDate fimDoMes = hoje.with(TemporalAdjusters.lastDayOfMonth());
        
        List<LancamentoModel> lancamentosDoMes = todosLancamentos.stream()
            .filter(l -> !l.getData().isBefore(inicioDoMes) && !l.getData().isAfter(fimDoMes))
            .collect(Collectors.toList());

        BigDecimal receitaMensal = BigDecimal.ZERO;
        BigDecimal despesaMensal = BigDecimal.ZERO;
        for (LancamentoModel l : lancamentosDoMes) {
            if ("Receita".equalsIgnoreCase(l.getTipo())) {
                receitaMensal = receitaMensal.add(l.getValor());
            } else if ("Despesa".equalsIgnoreCase(l.getTipo())) {
                despesaMensal = despesaMensal.add(l.getValor());
            }
        }
        
        BigDecimal saldoTotal = todosLancamentos.stream()
            .map(l -> "Receita".equalsIgnoreCase(l.getTipo()) ? l.getValor() : l.getValor().negate())
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        resumo.setSaldo(saldoTotal);
        resumo.setReceitaMensal(receitaMensal);
        resumo.setDespesaMensal(despesaMensal);
        resumo.setTransacoesMes(lancamentosDoMes);

        Map<Integer, BigDecimal> receitaPorMes = todosLancamentos.stream()
            .filter(l -> "Receita".equalsIgnoreCase(l.getTipo()) && l.getData().getYear() == hoje.getYear())
            .collect(Collectors.groupingBy(l -> l.getData().getMonthValue(), 
                                           Collectors.reducing(BigDecimal.ZERO, LancamentoModel::getValor, BigDecimal::add)));

        List<BigDecimal> receitaUltimosMeses = new ArrayList<>();
        for (int i = 1; i <= 12; i++) {
            receitaUltimosMeses.add(receitaPorMes.getOrDefault(i, BigDecimal.ZERO));
        }
        resumo.setReceitaUltimosMeses(receitaUltimosMeses);

        List<BigDecimal> receitaPorSemana = new ArrayList<>(Arrays.asList(BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO));
        List<BigDecimal> despesaPorSemana = new ArrayList<>(Arrays.asList(BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO));
        
        for (LancamentoModel l : lancamentosDoMes) {
            int diaDoMes = l.getData().getDayOfMonth();
            int semanaIndex = (diaDoMes - 1) / 7;
            if (semanaIndex > 3) semanaIndex = 3;

            if ("Receita".equalsIgnoreCase(l.getTipo())) {
                receitaPorSemana.set(semanaIndex, receitaPorSemana.get(semanaIndex).add(l.getValor()));
            } else if ("Despesa".equalsIgnoreCase(l.getTipo())) {
                despesaPorSemana.set(semanaIndex, despesaPorSemana.get(semanaIndex).add(l.getValor()));
            }
        }
        resumo.setReceitaMesAtual(receitaPorSemana);
        resumo.setDespesasMensais(despesaPorSemana);

        Map<String, BigDecimal> despesasPorCategoriaMap = lancamentosDoMes.stream()
            .filter(l -> "Despesa".equalsIgnoreCase(l.getTipo()))
            .collect(Collectors.groupingBy(l -> l.getCategoria().getNome(),
                                           Collectors.reducing(BigDecimal.ZERO, LancamentoModel::getValor, BigDecimal::add)));

        List<CategoriaDespesa> categoriasDespesa = new ArrayList<>();
        Map<String, String> coresFixas = Map.of(
            "Moradia", "#ef4444", "Alimentação", "#f97316", "Transporte", "#3b82f6",
            "Utilidades", "#8b5cf6", "Saúde", "#ec4899", "Outros", "#64748b",
            "Educação", "#FFD700", "Lazer", "#BA55D3", "Roupas", "#20B2AA"
        );
        
        despesasPorCategoriaMap.forEach((nome, valor) -> {
            categoriasDespesa.add(new CategoriaDespesa(nome, valor, coresFixas.getOrDefault(nome, "#A9A9A9")));
        });
        
        resumo.setCategoriasDespesa(categoriasDespesa);

        model.addAttribute("resumo", resumo);
        model.addAttribute("nomeUsuario", usuario.getNome());
        return "dashboard";
    }
}
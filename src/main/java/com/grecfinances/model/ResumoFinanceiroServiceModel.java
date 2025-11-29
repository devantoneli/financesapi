package com.grecfinances.model;

import java.math.BigDecimal;
import java.util.List;

public class ResumoFinanceiroServiceModel {

    private String nomeUsuario;
    private BigDecimal saldo;
    private BigDecimal receitaMensal;
    private BigDecimal despesaMensal;
    private List<BigDecimal> receitaUltimosMeses;
    private List<BigDecimal> receitaMesAtual;
    private List<BigDecimal> despesasMensais;
    private List<LancamentoModel> transacoesMes;
    private List<CategoriaDespesa> categoriasDespesa;

    // Getters e Setters
    public String getNomeUsuario() { return nomeUsuario; }
    public void setNomeUsuario(String nomeUsuario) { this.nomeUsuario = nomeUsuario; }

    public BigDecimal getSaldo() { return saldo; }
    public void setSaldo(BigDecimal saldo) { this.saldo = saldo; }

    public BigDecimal getReceitaMensal() { return receitaMensal; }
    public void setReceitaMensal(BigDecimal receitaMensal) { this.receitaMensal = receitaMensal; }

    public BigDecimal getDespesaMensal() { return despesaMensal; }
    public void setDespesaMensal(BigDecimal despesaMensal) { this.despesaMensal = despesaMensal; }

    public List<BigDecimal> getReceitaUltimosMeses() { return receitaUltimosMeses; }
    public void setReceitaUltimosMeses(List<BigDecimal> receitaUltimosMeses) { this.receitaUltimosMeses = receitaUltimosMeses; }

    public List<BigDecimal> getReceitaMesAtual() { return receitaMesAtual; }
    public void setReceitaMesAtual(List<BigDecimal> receitaMesAtual) { this.receitaMesAtual = receitaMesAtual; }

    public List<BigDecimal> getDespesasMensais() { return despesasMensais; }
    public void setDespesasMensais(List<BigDecimal> despesasMensais) { this.despesasMensais = despesasMensais; }

    public List<LancamentoModel> getTransacoesMes() { return transacoesMes; }
    public void setTransacoesMes(List<LancamentoModel> transacoesMes) { this.transacoesMes = transacoesMes; }

    public List<CategoriaDespesa> getCategoriasDespesa() { return categoriasDespesa; }
    public void setCategoriasDespesa(List<CategoriaDespesa> categoriasDespesa) { this.categoriasDespesa = categoriasDespesa; }

    // Métodos de cálculo existentes
    public BigDecimal calcularTotalReceitas(List<LancamentoModel> lancamentos){
        BigDecimal totalReceitas = BigDecimal.ZERO;
        for (LancamentoModel lancamento : lancamentos) {
            if ("receita".equalsIgnoreCase(lancamento.getTipo())) {
                totalReceitas = totalReceitas.add(lancamento.getValor());
            }
        }
        return totalReceitas;
    }

    public BigDecimal calcularTotalDespesas(List<LancamentoModel> lancamentos){
        BigDecimal totalDespesas = BigDecimal.ZERO;
        for (LancamentoModel lancamento : lancamentos) {
            if ("despesa".equalsIgnoreCase(lancamento.getTipo())) {
                totalDespesas = totalDespesas.add(lancamento.getValor());
            }
        }
        return totalDespesas;
    }

    public BigDecimal calcularSaldoFinal(List<LancamentoModel> lancamentos){
        BigDecimal saldoFinal = BigDecimal.ZERO;
        for (LancamentoModel lancamento : lancamentos) {
            if ("receita".equalsIgnoreCase(lancamento.getTipo())) {
                saldoFinal = saldoFinal.add(lancamento.getValor());
            } else if ("despesa".equalsIgnoreCase(lancamento.getTipo())) {
                saldoFinal = saldoFinal.subtract(lancamento.getValor());
            }
        }
        return saldoFinal;
    }

}

package com.grecfinances.model;

import java.math.BigDecimal;
import java.util.List;

public class ResumoFinanceiroServiceModel {

    private String nomeUsuario;
    private BigDecimal saldo;
    private BigDecimal receitaMensal;
    private BigDecimal despesaMensal;
    private List<BigDecimal> receitaUltimosMeses;

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

    // Métodos de cálculo existentes
    public BigDecimal calcularTotalReceitas(List<TransacaoModel> transacoes){
        BigDecimal totalReceitas = BigDecimal.ZERO;
        for (TransacaoModel transacao : transacoes) {
            if ("receita".equalsIgnoreCase(transacao.getTipo())) {
                totalReceitas = totalReceitas.add(transacao.getValor());
            }
        }
        return totalReceitas;
    }

    public BigDecimal calcularTotalDespesas(List<TransacaoModel> transacoes){
        BigDecimal totalDespesas = BigDecimal.ZERO;
        for (TransacaoModel transacao : transacoes) {
            if ("despesa".equalsIgnoreCase(transacao.getTipo())) {
                totalDespesas = totalDespesas.add(transacao.getValor());
            }
        }
        return totalDespesas;
    }

    public BigDecimal calcularSaldoFinal(List<TransacaoModel> transacoes){
        BigDecimal saldoFinal = BigDecimal.ZERO;
        for (TransacaoModel transacao : transacoes) {
            if ("receita".equalsIgnoreCase(transacao.getTipo())) {
                saldoFinal = saldoFinal.add(transacao.getValor());
            } else if ("despesa".equalsIgnoreCase(transacao.getTipo())) {
                saldoFinal = saldoFinal.subtract(transacao.getValor());
            }
        }
        return saldoFinal;
    }

}

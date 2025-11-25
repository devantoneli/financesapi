package br.com.grecfinances.financesapi.model;

import java.math.BigDecimal;
import java.util.List;

public class ResumoFinanceiroServiceModel {
    
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

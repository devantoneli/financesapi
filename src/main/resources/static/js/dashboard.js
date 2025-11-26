(function(){
    // Safely read the resumo object set by the inline Thymeleaf script
    const resumo = window.__RESUMO || { saldo:0, receitaMensal:0, despesaMensal:0, receitaUltimosMeses: [] };

    // Parse values (Thymeleaf will provide numbers, but double-check)
    const rawSaldo = Number(resumo.saldo) || 0;
    const rawReceita = Number(resumo.receitaMensal) || 0;
    const rawDespesa = Number(resumo.despesaMensal) || 0;
    const receitaData = Array.isArray(resumo.receitaUltimosMeses) ? resumo.receitaUltimosMeses.map(v=>Number(v)||0) : [];

    // Format BRL
    const brl = new Intl.NumberFormat('pt-BR', { style: 'currency', currency: 'BRL' });
    const sEl = document.getElementById('saldoValue'); if(sEl) sEl.textContent = brl.format(rawSaldo);
    const rEl = document.getElementById('receitaValue'); if(rEl) rEl.textContent = brl.format(rawReceita);
    const dEl = document.getElementById('despesaValue'); if(dEl) dEl.textContent = brl.format(rawDespesa);

    // Chart.js render
    const ctxEl = document.getElementById('receitaChart');
    if(ctxEl){
        const ctx = ctxEl.getContext('2d');
        new Chart(ctx, {
            type: 'bar',
            data: {
                labels: ['JAN','FEV','MAR','ABR','MAI','JUN','JUL','AGO','SET','OUT','NOV','DEZ'],
                datasets:[{
                    label:'Receita',
                    data: receitaData.length? receitaData : [0,0,0,0,0,0,0,0,0,0,0,0],
                    backgroundColor: 'rgba(94,166,255,0.85)',
                    borderRadius:6
                }]
            },
            options:{
                responsive:true,
                maintainAspectRatio:false,
                plugins:{ legend:{ display:false } },
                scales:{
                    x:{ grid:{ display:false }, ticks:{ color: 'rgba(255,255,255,0.8)' } },
                    y:{ grid:{ color: 'rgba(255,255,255,0.04)' }, ticks:{ color: 'rgba(255,255,255,0.8)' }, beginAtZero:true }
                }
            }
        });
    }
})();

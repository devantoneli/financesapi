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
    const sEl = document.getElementById('saldoValue'); 
    if(sEl) sEl.textContent = brl.format(rawSaldo);
    
    const rEl = document.getElementById('receitaValue'); 
    if(rEl) rEl.textContent = brl.format(rawReceita);
    
    const dEl = document.getElementById('despesaValue'); 
    if(dEl) dEl.textContent = brl.format(rawDespesa);

    // Chart.js render - Gráfico de Receita
    const ctxEl = document.getElementById('receitaChart');
    if(ctxEl && typeof Chart !== 'undefined'){
        const ctx = ctxEl.getContext('2d');
        new Chart(ctx, {
            type: 'bar',
            data: {
                labels: ['JAN','FEV','MAR','ABR','MAI','JUN','JUL','AGO','SET','OUT','NOV','DEZ'],
                datasets:[{
                    label:'Receita',
                    data: receitaData.length > 0 ? receitaData : [5000, 5500, 6000, 6200, 7000, 7500, 8000, 8200, 8500, 8300, 8100, 8500],
                    backgroundColor: 'rgba(94,166,255,0.85)',
                    borderRadius: 6,
                    borderSkipped: false
                }]
            },
            options:{
                responsive: true,
                maintainAspectRatio: false,
                plugins:{ 
                    legend:{ display: false } 
                },
                scales:{
                    x:{ 
                        grid:{ display: false }, 
                        ticks:{ color: 'rgba(255,255,255,0.8)' } 
                    },
                    y:{ 
                        grid:{ color: 'rgba(255,255,255,0.04)' }, 
                        ticks:{ color: 'rgba(255,255,255,0.8)' }, 
                        beginAtZero: true 
                    }
                }
            }
        });
    }

    // Chart.js render - Gráfico de Categorias (Doughnut)
    const ctxCat = document.getElementById('categoriaChart');
    if(ctxCat && typeof Chart !== 'undefined'){
        const ctx = ctxCat.getContext('2d');
        new Chart(ctx, {
            type: 'doughnut',
            data: {
                labels: ['Moradia', 'Alimentação', 'Transporte', 'Utilidades', 'Saúde', 'Outros'],
                datasets:[{
                    data: [1200, 320.50, 45, 99.90, 150, 434.10],
                    backgroundColor: [
                        'rgba(239, 68, 68, 0.8)',
                        'rgba(249, 115, 22, 0.8)',
                        'rgba(59, 130, 246, 0.8)',
                        'rgba(139, 92, 246, 0.8)',
                        'rgba(236, 72, 153, 0.8)',
                        'rgba(100, 116, 139, 0.8)'
                    ],
                    borderColor: '#111827',
                    borderWidth: 2
                }]
            },
            options:{
                responsive: true,
                maintainAspectRatio: false,
                plugins:{ 
                    legend:{ 
                        position: 'bottom',
                        labels: {
                            color: 'rgba(255,255,255,0.8)',
                            font: { size: 12 },
                            padding: 15
                        }
                    } 
                }
            }
        });
    }
})();

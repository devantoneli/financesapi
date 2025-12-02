(function(){ //DASHBOARD 1) Espera a página html ser carregada

    //DASHBOARD 2) É lido o objeto window e garante que exista e tenha as propriedades esperadas
    const resumo = window.__RESUMO || { 
        nomeUsuario: 'Usuário',
        saldo: 0, 
        receitaMensal: 0, 
        despesaMensal: 0, 
        receitaUltimosMeses: [],
        receitaMesAtual: [],
        despesasMensais: [],
        transacoesMes: [],
        categoriasDespesa: []
    };

    // --- Formatadores ---
    const brl = new Intl.NumberFormat('pt-BR', { style: 'currency', currency: 'BRL' });

    // --- Inicialização dos Valores do Topo ---
    function initializeSummaryValues() {
        const sEl = document.getElementById('saldoValue'); 
        if(sEl) sEl.textContent = brl.format(resumo.saldo || 0);
        
        const rEl = document.getElementById('receitaValue'); 
        if(rEl) rEl.textContent = brl.format(resumo.receitaMensal || 0);
        
        const dEl = document.getElementById('despesaValue'); 
        if(dEl) dEl.textContent = brl.format(resumo.despesaMensal || 0);
    }

    // --- Renderizadores de Gráficos ---

    //DASHBOARD 3) RENDERIZAÇÃO DOS GRÁFICOS
    //Gráfico de Receita Anual
    function renderReceitaAnualChart() {
        const ctxEl = document.getElementById('receitaChart');
        if(!ctxEl || typeof Chart === 'undefined') return;

        const receitas = resumo.receitaUltimosMeses || []; //Pega as listas de receitas
        const despesas = resumo.despesaUltimosMeses || []; //E de despesas
        
        //Calcula o balanço mensal de cada mês
        const balancoMensal = receitas.map((receita, index) => {
            const valorReceita = parseFloat(receita || 0);
            const valorDespesa = parseFloat(despesas[index] || 0);
            return valorReceita - valorDespesa;
        });

        //Cria o gráfico de barras colorido
        const backgroundColors = balancoMensal.map(valor => valor >= 0 ? 'rgba(74, 222, 128, 0.8)' : 'rgba(239, 68, 68, 0.8)');
                                                                             //Positivo = verde         Negativo = vermelho
        new Chart(ctxEl.getContext('2d'), {
            type: 'bar',
            data: {
                labels: ['JAN','FEV','MAR','ABR','MAI','JUN','JUL','AGO','SET','OUT','NOV','DEZ'],
                datasets:[{
                    label: 'Balanço Mensal',
                    data: balancoMensal,
                    backgroundColor: backgroundColors,
                    borderRadius: 6
                }]
            },
            options: getDefaultChartOptions()
        });
    }


    //Gráfico de Receita Mensal
    function renderReceitaMensalChart() {
        const ctxMesEl = document.getElementById('receitaMesChart');
        if(!ctxMesEl || typeof Chart === 'undefined') return;

        new Chart(ctxMesEl.getContext('2d'), {
            type: 'bar',
            data: {      // Cria o gráfico comparando receitas e despesas semanais
                labels: ['Semana 1', 'Semana 2', 'Semana 3', 'Semana 4'],
                datasets:[
                    {
                        label:'Receita (Mês)',
                        data: resumo.receitaMesAtual || [],
                        backgroundColor: 'rgba(74, 222, 128, 0.8)'
                    },
                    {
                        label:'Despesas (Mês)',
                        data: resumo.despesasMensais || [],
                        backgroundColor: 'rgba(239, 68, 68, 0.8)'
                    }
                ]
            },
            options: getDefaultChartOptions(true)
        });
    }

    //Gráfico de Categorias de Despesa
    function renderCategoriaChart() {
        const ctxCat = document.getElementById('categoriaChart'); 
        const legendContainer = document.getElementById('categoria-legend-list'); 
        if(!ctxCat || !legendContainer || typeof Chart === 'undefined') return; 

        const categorias = resumo.categoriasDespesa || []; 
        const labels = categorias.map(c => c.label);
        const data = categorias.map(c => c.value);
        const colors = categorias.map(c => c.color || '#64748b');
        
        const total = data.reduce((sum, value) => sum + value, 0); // Calcula o total de despesas

        let legendHtml = '';
        if (total > 0) {
            data.forEach((value, index) => { 
                const percentage = ((value / total) * 100).toFixed(1); // Calcula a porcentagem
                // Atribui o HTML da legenda
                legendHtml += ` 
                    <div class="legend-item">
                        <div class="legend-color-box" style="background-color: ${colors[index]}"></div>
                        <div class="legend-label">${labels[index]} (${percentage}%)</div>           
                        <div class="legend-value">${brl.format(value)}</div>
                    </div>
                `;
            });
        } else {
            legendHtml = '<p class="text-center text-muted small">Sem dados de despesa para exibir.</p>';
        }
        legendContainer.innerHTML = legendHtml; // Atualiza o contêiner da legenda com o HTML gerado

        new Chart(ctxCat.getContext('2d'), { // Cria o gráfico de rosca e configura as opções
            type: 'doughnut',
            data: {
                labels: labels,
                datasets:[{ data: data, backgroundColor: colors, borderColor: '#111827', borderWidth: 2 }] 
            },
            options:{
                responsive: true,
                maintainAspectRatio: false,
                plugins: { 
                    legend: { display: false },
                    tooltip: {
                        callbacks: {
                            label: function(context) {
                                const label = context.label || '';
                                const value = context.parsed;
                                const percentage = total > 0 ? ((value / total) * 100).toFixed(1) : '0.0';
                                return `${label}: ${percentage}%`;
                            }
                        }
                    }
                }
            }
        });
    }

    //DASHBOARD 4) GERAÇÃO DAS LISTAS
    //Tabela Mensal de Transações
    function generateMonthlyTable() {
        const container = document.getElementById('monthly-table-wrap'); // Verifica se o contêiner existe
        if (!container) return; 

        const transactions = resumo.transacoesMes || []; //Pega as transações do mês
        
        const groupedByDate = transactions.reduce((acc, tx) => { //Agrupa por data
            const dateStr = getFormattedDateString(tx.data); 
            if(dateStr) {
                (acc[dateStr] = acc[dateStr] || []).push(tx); 
            }
            return acc; 
        }, {});

        let html = '';
        const sortedDates = Object.keys(groupedByDate).sort((a, b) => new Date(a) - new Date(b)); //Ordena as datas

        //Gera o HTML para cada grupo de data
        if (sortedDates.length > 0) { //Se houver transações
            sortedDates.forEach(date => {
                // Adiciona 'T00:00:00' para evitar problemas de fuso horário ao criar a data para exibição
                html += `<div class="day-group"><div class="day-header">${new Date(date + 'T00:00:00').toLocaleDateString('pt-BR', {day:'2-digit', month:'short'})}</div>`;
                groupedByDate[date].forEach(tx => {
                    html += createTransactionItemHtml(tx); 
                });
                html += `</div>`;
            });
        } else { //Se não houver transações
            html = '<p class="text-center text-muted" style="padding: 2rem;"></p>';
        }
        container.innerHTML = html; //Atualiza o contêiner com o HTML gerado
    }

    // Lista de Entradas e Saídas Recentes
    function generateEntradasSaidasList() {
        const container = document.getElementById('entradas-saidas-list'); // Verifica se o contêiner existe
        if (!container) return;

        const transactions = resumo.transacoesMes || []; //Pega as transações do mês
        let html = ''; 
        if (transactions.length > 0) {
            transactions.slice()
                .sort((a, b) => new Date(getFormattedDateString(b.data)) - new Date(getFormattedDateString(a.data))) //Ordena por data decrescente
                .slice(0, 6) //Pega as 6 mais recentes
                .forEach(tx => { //Gera o HTML para cada transação
                    html += createTransactionItemHtml(tx); 
                });
        } else {
             html = '<p class="text-center text-muted small" style="padding: 2rem;">Nenhuma transação recente.</p>';
        }
        container.innerHTML = html; //Atualiza o contêiner com o HTML gerado
    }

    //DASHBOARD 5) TOGGLE DE VISÃO DE TABELA/GRÁFICO
    //Muda entre visão de gráfico e tabela mensal
    function setupViewToggle() {
        const btn = document.getElementById('toggle-view-btn');
        const chartView = document.getElementById('monthly-chart-view');
        const tableView = document.getElementById('monthly-table-view');
        
        if (!btn || !chartView || !tableView) return;

        btn.addEventListener('click', () => {
            chartView.classList.toggle('hidden');
            tableView.classList.toggle('hidden');
            
            const icon = btn.querySelector('i');
            if (chartView.classList.contains('hidden')) {
                icon.classList.remove('fa-table');
                icon.classList.add('fa-chart-bar');
            } else {
                icon.classList.remove('fa-chart-bar');
                icon.classList.add('fa-table');
            }
        });
    }

    // DASHBOARD 6) FUNÇÕES AUXILIARES
    // Formata um objeto de data para string 'YYYY-MM-DD'
    function getFormattedDateString(dateObj) {
        if (!dateObj) return null;
        if (typeof dateObj === 'string') {
            return dateObj.substring(0, 10);
        }
        if (Array.isArray(dateObj) && dateObj.length >= 3) {
            const [year, month, day] = dateObj;
            return `${year}-${String(month).padStart(2, '0')}-${String(day).padStart(2, '0')}`;
        }
        return null; 
    }
    
    //Gera o HTML de um item de transação
    function createTransactionItemHtml(tx) { 
        const isReceita = tx.tipo === 'Receita';
        const formattedAmount = (isReceita ? '+' : '-') + brl.format(tx.valor || 0);
        return `
            <div class="transaction-item">
                <div class="transaction-info">
                    <div class="transaction-title">${tx.descricao || 'N/A'}</div> 
                </div>
                <div class="transaction-amount ${isReceita ? 'receita' : 'despesa'}">${formattedAmount}</div>
            </div>
        `;
    }

    //Define as opções padrão para os gráficos
    function getDefaultChartOptions(showLegend = false) {
        return {
            responsive: true,
            maintainAspectRatio: false,
            plugins:{ 
                legend:{ display: showLegend, labels: { color: 'rgba(255,255,255,0.8)' } } 
            },
            scales:{
                x:{ grid:{ display: false }, ticks:{ color: 'rgba(255,255,255,0.8)' } },
                y:{ grid:{ color: 'rgba(255,255,255,0.04)' }, ticks:{ color: 'rgba(255,255,255,0.8)' }, beginAtZero: true }
            }
        };
    }

    //DASHBOARD 7) INICIALIZAÇÃO AO CARREGAR A PÁGINA
    document.addEventListener('DOMContentLoaded', () => {
        initializeSummaryValues();
        renderReceitaAnualChart();
        renderReceitaMensalChart();             //São chamadas para inicilizar os gráficos e listas
        renderCategoriaChart();
        generateMonthlyTable();
        generateEntradasSaidasList();
        setupViewToggle();
    });
})();
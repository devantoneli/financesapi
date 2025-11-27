// Configuração de paginação
const ITEMS_PER_PAGE = 5;
let currentPage = 1;
let allRows = [];
let activeFilters = [];

document.addEventListener('DOMContentLoaded', function() {
    // Obter todas as linhas da tabela
    allRows = Array.from(document.querySelectorAll('#lancamentosBody tr'));
    
    // Atualizar contagem total
    updateTotalCount();
    
    // Inicializar paginação
    updatePagination();
    
    // Adicionar event listeners aos botões de paginação
    document.getElementById('prevBtn').addEventListener('click', previousPage);
    document.getElementById('nextBtn').addEventListener('click', nextPage);
    
    // Adicionar event listeners aos filtros
    document.getElementById('filterIcon').addEventListener('click', openFilterModal);
    document.getElementById('clearAllFilters').addEventListener('click', clearAllFilters);

    // Adicionar event listener ao botão "Adicionar"
    document.querySelector('.btn-add').addEventListener('click', function() {
        window.location.href = '/lancamentos/novo';
    });
});

function updateTotalCount() {
    const totalCount = allRows.length;
    document.getElementById('totalCount').textContent = totalCount;
}

function updatePagination() {
    const totalRows = allRows.length;
    const totalPages = Math.ceil(totalRows / ITEMS_PER_PAGE);
    
    // Mostrar/esconder seção de paginação
    const paginationSection = document.getElementById('paginationSection');
    if (totalRows > ITEMS_PER_PAGE) {
        paginationSection.style.display = 'flex';
    } else {
        paginationSection.style.display = 'none';
    }

    // Atualizar números de página
    document.getElementById('currentPage').textContent = currentPage;
    document.getElementById('totalPages').textContent = totalPages;

    // Desabilitar botões de navegação conforme necessário
    document.getElementById('prevBtn').disabled = currentPage === 1;
    document.getElementById('nextBtn').disabled = currentPage === totalPages;

    // Mostrar/esconder linhas
    const startIndex = (currentPage - 1) * ITEMS_PER_PAGE;
    const endIndex = startIndex + ITEMS_PER_PAGE;

    allRows.forEach((row, index) => {
        if (index >= startIndex && index < endIndex) {
            row.style.display = '';
        } else {
            row.style.display = 'none';
        }
    });
}

function previousPage() {
    const totalPages = Math.ceil(allRows.length / ITEMS_PER_PAGE);
    if (currentPage > 1) {
        currentPage--;
        updatePagination();
    }
}

function nextPage() {
    const totalPages = Math.ceil(allRows.length / ITEMS_PER_PAGE);
    if (currentPage < totalPages) {
        currentPage++;
        updatePagination();
    }
}

// Aplicar estilos aos botões desabilitados
const style = document.createElement('style');
style.textContent = `
    .pagination-btn:disabled {
        opacity: 0.5;
        cursor: not-allowed;
    }
    .pagination-btn:disabled:hover {
        background: rgba(255, 255, 255, 0.08) !important;
        color: var(--muted) !important;
    }
`;
document.head.appendChild(style);

// Funções de Filtro
function openFilterModal() {
    const filterInput = prompt('Digite o filtro que deseja adicionar:');
    if (filterInput && filterInput.trim() !== '') {
        addFilter(filterInput.trim());
    }
}

function addFilter(filterName) {
    if (!activeFilters.includes(filterName)) {
        activeFilters.push(filterName);
        updateFiltersDisplay();
    }
}

function removeFilter(filterName) {
    activeFilters = activeFilters.filter(f => f !== filterName);
    updateFiltersDisplay();
}

function clearAllFilters() {
    activeFilters = [];
    updateFiltersDisplay();
}

function updateFiltersDisplay() {
    const filtersSection = document.getElementById('filtersSection');
    const filtersContainer = document.getElementById('filtersContainer');
    const filterCount = document.getElementById('filterCount');
    
    filterCount.textContent = activeFilters.length;
    
    // Limpar container
    filtersContainer.innerHTML = '';
    
    // Adicionar filtros
    activeFilters.forEach(filter => {
        const chip = document.createElement('span');
        chip.className = 'filter-chip';
        chip.innerHTML = `${filter} <span class="close-filter">×</span>`;
        chip.querySelector('.close-filter').addEventListener('click', function() {
            removeFilter(filter);
        });
        filtersContainer.appendChild(chip);
    });
    
    // Mostrar/esconder seção de filtros
    if (activeFilters.length > 0) {
        filtersSection.style.display = 'flex';
    } else {
        filtersSection.style.display = 'none';
    }
}

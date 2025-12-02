document.addEventListener('DOMContentLoaded', function () {
    const addModal = document.getElementById('addLancamentoModal');
    if (addModal) {
        addModal.addEventListener('show.bs.modal', function (event) {
            // Limpa o formulário ao abrir o modal
            const form = document.getElementById('addLancamentoForm');
            form.reset();
        });
    }

    const addLancamentoForm = document.getElementById('addLancamentoForm');
    if (addLancamentoForm) {
        addLancamentoForm.addEventListener('submit', function (event) {
            // INSERT 4 - modal.js impede o recarregamento da página e monta um objeto JSON com os dados.
            event.preventDefault();
            const formData = new FormData(addLancamentoForm);
            const data = Object.fromEntries(formData.entries());

            // INSERT 5 - É feita uma requisição POST /api/lancamentos usando fetch() para o front enviar os dados para o back (segue no LancamentoController).
            fetch('/api/lancamentos', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(data),
            })
            .then(response => response.json())
            .then(data => {
                if (data.id) {
                    // INSERT 11 -  Fecha o modal
                    const modal = bootstrap.Modal.getInstance(addModal);
                    modal.hide();

                    // INSERT 12 - Recarrega a página para mostrar o novo lançamento
                    location.reload();
                } else {
                    // Exibe uma mensagem de erro
                    const errorDiv = addModal.querySelector('.alert-danger');
                    errorDiv.textContent = 'Erro ao salvar o lançamento.';
                    errorDiv.style.display = 'block';
                }
            })
            .catch(error => {
                console.error('Erro:', error);
                const errorDiv = addModal.querySelector('.alert-danger');
                errorDiv.textContent = 'Erro ao salvar o lançamento.';
                errorDiv.style.display = 'block';
            });
        });
    }
});


// UPDATE 2 - Script para modal de edição de lançamentos
        const editModal = document.getElementById('editModal');
        editModal.addEventListener('show.bs.modal', event => {
            // Botão que acionou o modal
            const button = event.relatedTarget;
            // Extrai o ID do atributo data-*
            const lancamentoId = button.getAttribute('data-lancamento-id');

            // URL da API para buscar os dados
            const apiUrl = `/api/lancamentos/${lancamentoId}`;

            // Busca os dados do lançamento
            fetch(apiUrl)
                .then(response => response.json())
                .then(data => {
                    if (data) {
                        // Popula o formulário no modal
                        const form = document.getElementById('editForm');
                        form.querySelector('#edit-id').value = data.id;
                        form.querySelector('#edit-descricao').value = data.descricao;
                        form.querySelector('#edit-valor').value = data.valor;
                        form.querySelector('#edit-data').value = data.data;
                        form.querySelector('#edit-tipo').value = data.tipo;
                        form.querySelector('#edit-categoria').value = data.categoria.id;
                    }
                })
                .catch(error => console.error('Erro ao buscar dados do lançamento:', error));
        });
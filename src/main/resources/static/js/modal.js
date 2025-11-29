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
            event.preventDefault();

            const formData = new FormData(addLancamentoForm);
            const data = Object.fromEntries(formData.entries());

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
                    // Esconde o modal
                    const modal = bootstrap.Modal.getInstance(addModal);
                    modal.hide();

                    // Recarrega a página para mostrar o novo lançamento
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

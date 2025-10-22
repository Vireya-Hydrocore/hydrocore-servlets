// Pegando os valores do HTML
const openModal = document.getElementById('openModal'); // botão que abre o modal
const closeModal = document.getElementById('closeModal'); // o X pra fechar
const modal = document.getElementById('modal'); // o modal em si
const cancelBtn = document.getElementById('cancelBtn'); // botão de cancelar lá dentro

// Quando clicar no botão, o modal aparece
openModal.addEventListener('click', () => {
    modal.style.display = 'flex'; // mostra o modal
});

// Quando clicar no X, o modal some
closeModal.addEventListener('click', () => {
    modal.style.display = 'none'; // esconde o modal
});

// Se clicar no botão de cancelar, também some
cancelBtn.addEventListener('click', () => {
    modal.style.display = 'none';
});

// Se clicar fora do modal (tipo na parte escura), ele fecha também
window.addEventListener('click', (e) => {
    if (e.target === modal) { // confere se o clique foi fora do conteúdo
        modal.style.display = 'none';
    }
});

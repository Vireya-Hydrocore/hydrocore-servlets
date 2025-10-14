
const openModal = document.getElementById('openModal');
const closeModal = document.getElementById('closeModal');
const modal = document.getElementById('modal');
const cancelBtn = document.getElementById('cancelBtn');

// Abrir modal
openModal.addEventListener('click', () => {
    modal.style.display = 'flex';
});

// Fechar modal
closeModal.addEventListener('click', () => {
    modal.style.display = 'none';
});

cancelBtn.addEventListener('click', () => {
    modal.style.display = 'none';
});

// Fechar clicando fora do modal
window.addEventListener('click', (e) => {
    if (e.target === modal) {
        modal.style.display = 'none';
    }
});
const menuBtn = document.getElementById("menu-toggle");
const sidebar = document.getElementById("sidebar");
const avatar = document.getElementById("avatar");
const dropdown = document.getElementById("dropdown");

// ----- MENU HAMBÚRGUER -----
menuBtn.addEventListener("click", () => {
    sidebar.classList.toggle("open");
});

// ----- PERFIL (AVATAR) -----
avatar.addEventListener("click", (e) => {
    e.stopPropagation(); // evita fechar instantaneamente
    dropdown.classList.toggle("show");
});

// Fecha o dropdown se clicar fora
window.addEventListener("click", (e) => {
    if (!avatar.contains(e.target) && !dropdown.contains(e.target)) {
        dropdown.classList.remove("show");
    }
});

// Alterar o tipo do input do filtro
function dataFiltro(){
    const colunaSelect = document.getElementById('colunaSelect');
    const inputPesquisa = document.getElementById('pesquisa');

    colunaSelect.addEventListener('change', function() {
        const valor = colunaSelect.value;

        // Se for uma das opções de data, muda o tipo para "date"
        if (valor === 'data_admissao' || valor === 'data_nascimento') {
            inputPesquisa.type = 'date';
            inputPesquisa.value = ''; // limpa o campo para evitar erro de formato
        } else {
            inputPesquisa.type = 'text';
            inputPesquisa.value = '';
        }
    });
}

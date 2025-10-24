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


const senhaInput = document.getElementById("senha");
const lengthItem = document.getElementById("length");
const uppercaseItem = document.getElementById("uppercase");
const specialItem = document.getElementById("special");
const numberItem = document.getElementById("number");

senhaInput.addEventListener("input", function () {
    const senha = senhaInput.value;

// 1 Verifica comprimento
    if (senha.length >= 8) {
        lengthItem.classList.add("valid");
        lengthItem.classList.remove("invalid");
    } else {
        lengthItem.classList.add("invalid");
        lengthItem.classList.remove("valid");
    }

// 2 Verifica letra maiúscula
    if (/[A-Z]/.test(senha)) {
        uppercaseItem.classList.add("valid");
        uppercaseItem.classList.remove("invalid");
    } else {
        uppercaseItem.classList.add("invalid");
        uppercaseItem.classList.remove("valid");
    }

// 3 Verifica caractere especial
    if (/[!@#$%]/.test(senha)) {
        specialItem.classList.add("valid");
        specialItem.classList.remove("invalid");
    } else {
        specialItem.classList.add("invalid");
        specialItem.classList.remove("valid");
    }
// 4 Verifica números
    if(/[0-9]/.test(senha)){
        numberItem.classList.add("valid");
        numberItem.classList.remove("invalid");
    } else {
        numberItem.classList.add("invalid");
        numberItem.classList.remove("valid");
    }
});
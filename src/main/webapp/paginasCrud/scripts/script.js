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
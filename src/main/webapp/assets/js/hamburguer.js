// Pega o botão do menu
const menuBtn = document.getElementById("menu-toggle");
// Pega o menu lateral
const sidebar = document.getElementById("sidebar");

// Quando clicar no botão do menu, ele adiciona ou tira a classe "open"
// Isso faz o menu aparecer ou sumir
menuBtn.addEventListener("click", () => {
    sidebar.classList.toggle("open");
});

// const avatar = document.getElementById('avatar');
// const dropdown = document.getElementById('dropdown');
//
// avatar.addEventListener('click', () => {
//     dropdown.style.display = dropdown.style.display === 'block' ? 'none' : 'block';
// });
//
// document.addEventListener('click', (e) => {
//     if (!avatar.contains(e.target) && !dropdown.contains(e.target)) {
//         dropdown.style.display = 'none';
//     }
// });
const menuBtn = document.getElementById("menu-toggle");
const sidebar = document.getElementById("sidebar");
menuBtn.addEventListener("click", () => {
    sidebar.classList.toggle("open");
});

const avatar = document.getElementById('avatar');
const dropdown = document.getElementById('dropdown');

avatar.addEventListener('click', () => {
    dropdown.style.display = dropdown.style.display === 'block' ? 'none' : 'block';
});

document.addEventListener('click', (e) => {
    if (!avatar.contains(e.target) && !dropdown.contains(e.target)) {
        dropdown.style.display = 'none';
    }
});
// Quando clicar no ícone de olho (botão com id "togglePassword"), mostra a senha
document.getElementById('togglePassword').addEventListener('click', function () {
    // Pega o campo de senha
    const passwordInput = document.getElementById('senha');
    // Pega o ícone dentro do botão (o olho)
    const eyeIcon = this.querySelector('i');
    // Se o campo estiver no como password escondendo a senha)
    if (passwordInput.type === 'password') {
        // Muda para text pra mostrar a senha
        passwordInput.type = 'text';
        // Troca o icone do olho fechado pro olho aberto
        eyeIcon.classList.remove('fa-eye-slash');
        eyeIcon.classList.add('fa-eye');
    } else {
        // Se já estiver mostrando a senha, volta pro modo password
        passwordInput.type = 'password';
        // Troca o icone de volta pro olho fechado
        eyeIcon.classList.remove('fa-eye');
        eyeIcon.classList.add('fa-eye-slash');
    }
});

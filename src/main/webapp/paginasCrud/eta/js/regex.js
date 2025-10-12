const telefone = document.getElementById('telefone');
const cnpj = document.getElementById('cnpj');
const erroSenha = document.getElementById("erroSenha");

// Máscara de Telefone
telefone.addEventListener('input', function () {
    let valor = telefone.value;
    valor = valor.replace(/\D/g, "");

    if (valor.length > 10) {
        valor = valor.replace(/(\d{2})(\d{5})(\d{4}).*/, "($1) $2-$3");
    } else if (valor.length > 6) {
        valor = valor.replace(/(\d{2})(\d{4})(\d{0,4}).*/, "($1) $2-$3");
    } else if (valor.length > 2) {
        valor = valor.replace(/(\d{2})(\d{0,5})/, "($1) $2");
    } else {
        valor = valor.replace(/(\d*)/, "($1");
    }

    telefone.value = valor;
});

// Máscara de CNPJ
cnpj.addEventListener('input', function () {
    let valor = cnpj.value;
    valor = valor.replace(/\D/g, ""); // só números

    // Limita a 14 dígitos (CNPJ válido)
    valor = valor.substring(0, 14);

    // Aplica a máscara
    valor = valor.replace(/^(\d{2})(\d)/, "$1.$2");
    valor = valor.replace(/^(\d{2})\.(\d{3})(\d)/, "$1.$2.$3");
    valor = valor.replace(/\.(\d{3})(\d)/, ".$1/$2");
    valor = valor.replace(/(\d{4})(\d)/, "$1-$2");

    cnpj.value = valor;
});




cep.addEventListener('input', function () {
    let valor = cep.value;
    valor = valor.replace(/\D/g, ""); // mantém só números

    // Limita a 8 dígitos
    valor = valor.substring(0, 8);

    // Aplica a máscara: 00000-000
    if (valor.length > 5) {
        valor = valor.replace(/(\d{5})(\d{0,3})/, "$1-$2");
    }

    cep.value = valor;
});
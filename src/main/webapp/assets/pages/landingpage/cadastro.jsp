<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html lang="pt-br">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Cadastre-se</title>
    <link rel="shortcut icon" href="${pageContext.request.contextPath}/assets/imgs/vireya%20icon.png" type="image/x-icon">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/cadastro.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
</head>

<body>
<main>
    <div class="container-esquerdo">
        <div class="logo">
            <img src="${pageContext.request.contextPath}/assets/imgs/vireya%20icon.png">
            <span>HydroCore</span>
        </div>
        <h1>Criar Conta</h1>

        <form action="cadastro2.jsp" method="post" id="form">
            <label for="nome">Nome</label>
            <input name="nome" type="text" id="nome" placeholder="Digite seu nome">
            <div id="erroNome" class="erro"></div>

            <label for="email">E-mail</label>
            <input name="email" type="email" id="email" placeholder="Digite seu e-mail">
            <div id="erroEmail" class="erro"></div>

            <label for="cnpj">CNPJ</label>
            <input name="cnpj" type="text" id="cnpj" placeholder="Digite seu CNPJ"
                   pattern="[0-9]{2}\.[0-9]{3}\.[0-9]{3}/[0-9]{4}-[0-9]{2}">
            <div id="erroCNPJ" class="erro"></div>

            <label for="senha">Senha</label>
            <div class="input-senha">
                <input name="senha" type="password" id="senha" placeholder="Digite sua senha" required
                       pattern="^(?=.*[A-Z])(?=.*[!@#$%]).{8,}$"
                       title="Tem que incluir pelo menos uma letra maiúscula e um caractere especial(!@#$%)">
                <span class="password-toggle" id="togglePassword">
                        <i class="fas fa-eye"></i>
                    </span>
            </div>
            <div id="erroSenha" class="erro"></div>

            <label for="telefone">Telefone</label>
            <input name="telefone" type="text" id="telefone" placeholder="Digite o telefone (99) 99999-9999"
                   pattern="\([0-9]{2}\) [0-9]{5}-[0-9]{4}">
            <div id="erroTelefone" class="erro"></div>

            <label for="capacidade">Capacidade</label>
            <input name="capacidade" type="number" id="capacidade" placeholder="1000">
            <div id="erroCapacidade" class="erro"></div>

            <div class="checkbox">
                <input type="checkbox" id="termos">
                <label for="termos">Termos e Condições de Uso</label>
            </div>
            <button class="botao" type="submit">Cadastre-se</button>
            <p class="login-text">Já possui uma conta? <a href="${pageContext.request.contextPath}/assets/pages/landingpage/login.jsp">Clique aqui</a></p>
        </form>
    </div>

    <div class="container-direito">
        <div class="sobreposicao">
            <img src="${pageContext.request.contextPath}/assets/imgs/abstract%20geometric.png">
        </div>
        <div class="celular">
            <img src="${pageContext.request.contextPath}/assets/imgs/modelo_frontal.png">
        </div>
    </div>

    <script src="${pageContext.request.contextPath}/assets/js/regex.js"></script>
    <script src="${pageContext.request.contextPath}/assets/js/script.js"></script>
</main>
</body>
</html>
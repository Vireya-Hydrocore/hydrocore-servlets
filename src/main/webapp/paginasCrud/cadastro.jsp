<%--
  Created by IntelliJ IDEA.
  User: eriksilva-ieg
  Date: 08/10/2025
  Time: 21:22
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="pt-br">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Cadastre-se</title>
    <link rel="shortcut icon" href="<%= request.getContextPath() %>/assets/img/vireya icon.png" type="image/x-icon">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/paginasCrud/css/cadastro.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
</head>

<body>
<main>
    <div class="container-esquerdo">
        <div class="logo">
            <img src="<%= request.getContextPath() %>/assets/img/vireya icon.png" alt="Logo">
            <span>HydroCore</span>
        </div>

        <h1>Criar Conta</h1>

        <%
            String erro = (String) request.getAttribute("erro");
            String sucesso = (String) request.getAttribute("sucesso");
            if (erro != null) {
        %>
        <div class="erro" style="color: red; text-align: center;"><%= erro %></div>
        <% } else if (sucesso != null) { %>
        <div style="color: green; text-align: center;"><%= sucesso %></div>
        <% } %>

        <form id="form" action="<%= request.getContextPath() %>/ServletEta?action=" method="post">
            <label for="nome">Nome</label>
            <input type="text" id="nome" name="nome" placeholder="Digite seu nome" required>
            <div id="erroNome" class="erro"></div>

            <label for="email">E-mail</label>
            <input type="email" id="email" name="email" placeholder="Digite seu e-mail" required>
            <div id="erroEmail" class="erro"></div>

            <label for="cnpj">CNPJ</label>
            <input type="text" id="cnpj" name="cnpj" placeholder="Digite seu CNPJ"
                   pattern="[0-9]{2}\.[0-9]{3}\.[0-9]{3}/[0-9]{4}-[0-9]{2}" required>
            <div id="erroCNPJ" class="erro"></div>

            <label for="senha">Senha</label>
            <div class="input-senha">
                <input type="password" id="senha" name="senha" placeholder="Digite sua senha" required
                       pattern="^(?=.*[A-Z])(?=.*[!@#$%]).{8,}$"
                       title="Tem que incluir pelo menos uma letra maiúscula e um caractere especial (!@#$%)">
                <span class="password-toggle" id="togglePassword">
                        <i class="fas fa-eye"></i>
                    </span>
            </div>
            <div id="erroSenha" class="erro"></div>

            <label for="telefone">Telefone</label>
            <input type="text" id="telefone" name="telefone" placeholder="(99) 99999-9999"
                   pattern="\([0-9]{2}\) [0-9]{5}-[0-9]{4}" required>
            <div id="erroTelefone" class="erro"></div>

            <div class="checkbox">
                <input type="checkbox" id="termos" name="termos" required>
                <label for="termos">Termos e Condições de Uso</label>
            </div>

            <button class="botao" type="submit">Cadastre-se</button>
            <p class="login-text">Já possui uma conta?
                <a href="<%= request.getContextPath() %>/paginas/login.jsp">Clique aqui</a>
            </p>
        </form>
    </div>

    <div class="container-direito">
        <div class="sobreposicao">
            <img src="<%= request.getContextPath() %>/assets/img/abstract geometric.png" alt="Background">
        </div>
        <div class="celular">
            <img src="<%= request.getContextPath() %>/assets/img/modelo_frontal.png" alt="Celular">
        </div>
    </div>

    <script src="<%= request.getContextPath() %>/assets/js/regex.js"></script>
    <script src="<%= request.getContextPath() %>/assets/js/script.js"></script>
</main>
</body>
</html>

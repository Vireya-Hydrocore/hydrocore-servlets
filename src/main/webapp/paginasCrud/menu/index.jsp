<%--
  Created by IntelliJ IDEA.
  User: eriksilva-ieg
  Date: 09/09/2025
  Time: 20:55
  To change this template use File | Settings | File Templates.
--%>

<!------------------ MENU GERAL ------------------->
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <title>Menu - Área Restrita</title>
    <style>
        body {
            font-family: "Segoe UI", Tahoma, Geneva, Verdana, sans-serif;
            background: linear-gradient(135deg, #4460F6, #2c43b3);
            margin: 0;
            padding: 0;
            min-height: 100vh;
            display: flex;
            flex-direction: column;
            align-items: center;
            justify-content: center;
            color: #fff;
        }

        h1 {
            margin-bottom: 10px;
            font-size: 2.2em;
        }

        h2 {
            margin-bottom: 30px;
            font-weight: 400;
        }

        nav {
            display: flex;
            flex-wrap: wrap;
            justify-content: center;
            gap: 20px;
            max-width: 800px;
        }

        nav a {
            background-color: #fff;
            color: #2c43b3;
            text-decoration: none;
            padding: 15px 25px;
            border-radius: 10px;
            font-weight: bold;
            font-size: 1.1em;
            box-shadow: 0 4px 8px rgba(0,0,0,0.2);
            transition: all 0.25s ease;
        }

        nav a:hover {
            background-color: #2c43b3;
            color: #fff;
            transform: translateY(-3px);
        }

        .disabled {
            background-color: #cccccc;
            color: #666666;
            cursor: not-allowed;
            pointer-events: none;
        }

        footer {
            margin-top: 40px;
            font-size: 0.9em;
            opacity: 0.8;
        }
    </style>
</head>
<body>

<h1>CRUD VIREYA</h1>
<h2>Selecione a tabela para gerenciar:</h2>

<nav>
    <!-- Funcionário -->
    <a href="${pageContext.request.contextPath}/ServletFuncionario?action=mainFuncionario">
        Funcionário
    </a>

    <!-- Cargo (em breve) -->
    <a href="${pageContext.request.contextPath}/ServletCargo?action=mainCargo">Cargo</a>

    <!-- Produto -->
    <a href="${pageContext.request.contextPath}/ServletProduto?action=mainProduto">
        Produto
    </a>

    <!-- Estoque -->
    <a href="${pageContext.request.contextPath}/ServletEstoque?action=mainEstoque">
        Estoque
    </a>

    <!-- ETA (em breve) -->
    <a href="#" class="disabled">ETA - Em Breve</a>

    <!-- Admin (em breve) -->
    <a href="${pageContext.request.contextPath}/paginasCrud/admin/logar.jsp">Admin - Em Breve</a>
</nav>
<form action="${pageContext.request.contextPath}/ServletAdmin" method="post">
    <!-- O servlet vai identificar a ação -->
    <input type="hidden" name="action" value="logar">

    <!-- Campo de E-mail -->
    <div class="campos">
        <label for="email">E-mail</label>
        <input type="email" id="email" name="email" placeholder="Digite seu e-mail" required>
    </div>

    <!-- Campo de Senha -->
    <div class="campos">
        <label for="senha">Senha</label>
        <input type="password" id="senha" name="senha" placeholder="Digite sua senha" required>
    </div>

    <!-- Botão -->
    <div class="acoes">
        <input type="submit" value="Entrar">
    </div>
</form>
<footer>
    © 2025 Vireya — Sistema de Gestão CRUD
</footer>

</body>
</html>

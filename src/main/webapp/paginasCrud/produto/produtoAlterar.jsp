<%--
  Created by IntelliJ IDEA.
  User: eriksilva-ieg
  Date: 06/10/2025
  Time: 14:03
  To change this template use File | Settings | File Templates.
--%>
<%--
  Página: produtoAlterar.jsp
  Autor: Erik Silva
  Data: 06/10/2025
  Descrição: Formulário de alteração de produto
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <title>Alterar Produto</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background: #f5f5f5;
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
        }
        .form-container {
            background: #fff;
            padding: 20px 30px;
            border-radius: 10px;
            box-shadow: 0 2px 6px rgba(0,0,0,0.2);
            width: 400px;
        }
        .form-container h2 {
            text-align: center;
            margin-bottom: 20px;
        }
        .campos {
            margin-bottom: 15px;
        }
        .campos label {
            display: block;
            margin-bottom: 5px;
            font-weight: bold;
        }
        .campos input,
        .campos select {
            width: 100%;
            padding: 8px;
            border: 1px solid #bbb;
            border-radius: 6px;
        }
        .acoes {
            text-align: center;
            margin-top: 20px;
        }
        .acoes input[type="submit"] {
            background-color: #4460F6;
            color: #fff;
            border: none;
            padding: 10px 15px;
            border-radius: 6px;
            cursor: pointer;
        }
        .acoes input[type="submit"]:hover {
            background-color: #2f45b5;
        }
    </style>
</head>
<body>

<div class="form-container">
    <h2>Alterar Produto</h2>

    <form action="${pageContext.request.contextPath}/ServletProduto" method="post">
        <!-- O servlet vai identificar a ação pelo nome -->
        <input type="hidden" name="action" value="update">

        <!-- ID do produto -->
        <div class="campos">
            <label>ID</label>
            <input type="number" name="id" value="${produto.id}" readonly>
        </div>

        <!-- Nome -->
        <div class="campos">
            <label>Nome</label>
            <input type="text" name="nome" value="${produto.nome}" required>
        </div>

        <!-- Tipo -->
        <div class="campos">
            <label>Tipo</label>
            <input type="text" name="tipo" value="${produto.tipo}" required>
        </div>

        <!-- Unidade de Medida -->
        <div class="campos">
            <label>Unidade de Medida</label>
            <input type="text" name="unidadeMedida" value="${produto.unidadeMedida}" required>
        </div>

        <!-- Concentração -->
        <div class="campos">
            <label>Concentração</label>
            <input type="number" step="0.01" name="concentracao" value="${produto.concentracao}" required>
        </div>

        <!-- Botão -->
        <div class="acoes">
            <input type="submit" value="Salvar Alterações">
        </div>
    </form>
</div>

</body>
</html>

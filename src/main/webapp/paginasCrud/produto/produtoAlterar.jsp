<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.example.servletsvireya.model.Produto" %>

<%
    Produto produto = (Produto) request.getAttribute("produtoSelecionado");
%>

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
        form {
            background: white;
            padding: 20px;
            border-radius: 10px;
            width: 400px;
            box-shadow: 0 2px 6px rgba(0,0,0,0.2);
        }
        h2 {
            text-align: center;
            margin-bottom: 15px;
        }
        label {
            display: block;
            margin-top: 10px;
            font-weight: bold;
        }
        input, select {
            width: 100%;
            padding: 8px;
            margin-top: 5px;
            border: 1px solid #ccc;
            border-radius: 5px;
        }
        .acoes {
            text-align: center;
            margin-top: 15px;
        }
        .acoes input {
            background: #4460F6;
            color: white;
            border: none;
            padding: 10px 15px;
            border-radius: 5px;
            cursor: pointer;
        }
        .acoes input:hover {
            background: #2f45b5;
        }
    </style>
</head>
<body>

<form action="${pageContext.request.contextPath}/ServletProduto" method="post">
    <h2>Alterar Produto</h2>

    <input type="hidden" name="action" value="updateProduto">
    <input type="hidden" name="id" value="<%= produto.getId() %>">

    <label>Nome</label>
    <input type="text" name="nome" value="<%= produto.getNome() %>" required>

    <label>Categoria</label>
    <input type="text" name="tipo" value="<%= produto.getTipo() %>" required>

    <label>Unidade de Medida</label>
    <input type="text" name="unidadeMedida" value="<%= produto.getUnidadeMedida() %>" required>

    <label>Concentração (%)</label>
    <input type="number" step="0.01" name="concentracao" value="<%= produto.getConcentracao() %>" required>

    <div class="acoes">
        <input type="submit" value="Salvar Alterações">
    </div>
</form>

</body>
</html>

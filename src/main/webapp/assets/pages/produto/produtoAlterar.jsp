<%--
  Created by IntelliJ IDEA.
  User: eriksilva-ieg
  Date: 06/10/2025
  Time: 14:03
  To change this template use File | Settings | File Templates.
--%>
<%@ page import="com.example.servletsvireya.dto.ProdutoDTO" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    ProdutoDTO produto = (ProdutoDTO) request.getAttribute("produto");
%>

<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <title>Produto</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/styleAlterar.css">
</head>
<body>

<div class="form-container">
    <h2>Alterar Produto</h2>

    <form action="${pageContext.request.contextPath}/ServletProduto" method="post">
        <input type="hidden" name="action" value="updateProduto">

        <!-- ID do produto -->
        <div class="campos-readonly">
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
            <select name="tipo">
                <option value="${produto.tipo}">${produto.tipo}</option>
                <option value="Coagulante">Coagulante</option>
                <option value="Floculante">Floculante</option>
            </select>
        </div>

        <!-- Unidade de Medida -->
        <div class="campos">
            <label>Unidade de Medida</label>
            <select name="unidadeMedida">
                <option value="${produto.unidadeMedida}">${produto.unidadeMedida}</option>
                <option value="kg">kg</option>
                <option value="g">g</option>
                <option value="L">L</option>
                <option value="mL">mL</option>
                <option value="mg/L">mg/L</option>
                <option value="µg/L">µg/L</option>
            </select>
        </div>

        <!-- Concentração -->
        <div class="campos">
            <label>Concentração</label>
            <input type="number" name="concentracao" value="${produto.concentracao}" required>
        </div>

        <!-- Nome ETA -->
        <div class="campos-readonly">
            <label>ETA</label>
            <input type="text" name="nomeEta" value="${produto.nomeEta}" readonly>
        </div>

        <!-- Botão -->
        <div class="acoes">
            <input type="submit" value="Salvar Alterações">
        </div>
    </form>
</div>

</body>
</html>
<%--
  Created by IntelliJ IDEA.
  User: iagodiniz-ieg
  Date: 05/10/2025
  Time: 16:18
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.example.servletsvireya.dto.CargoDTO" %>

<%
    CargoDTO cargo = (CargoDTO) request.getAttribute("cargo");
%>

<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <title>Cargo</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/styleAlterar.css">
</head>
<body>

<div class="form-container">
    <h2>Alterar Cargo</h2>

    <form action="${pageContext.request.contextPath}/ServletCargo" method="post">
        <input type="hidden" name="action" value="updateCargo">
        <div class="campos">
            <input type="hidden" name="id" value="${cargo.id}" readonly>
        </div>

        <div class="campos">
            <label for="cargo">Nome do cargo:</label>
            <input type="text" id="cargo" name="nome" value="${cargo.nome}" required><br>
        </div>

        <div class="campos">
            <label for="acesso">Nível de acesso:</label>
            <input type="number" id="acesso" name="acesso"  value="${cargo.acesso}" required><br>
        </div>

        <div class="acoes">
            <input type="submit" value="Salvar alterações">
        </div>
    </form>

</div>

</body>
</html>
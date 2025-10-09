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
    CargoDTO cargo = (CargoDTO) request.getAttribute("cargoSelecionado");
%>

<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <title>Cargo</title>
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
        .campos input {
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
    <h2>Alterar Cargo</h2>

    <form action="${pageContext.request.contextPath}/ServletCargo" method="post">
        <input type="hidden" name="action" value="updateCargo">
        <input type="hidden" name="id" value="${cargoSelecionado.id}" readonly>

        <label for="cargo">Nome do cargo:</label>
        <input type="text" id="cargo" name="nomeCargo" value="${cargoSelecionado.nome}" required><br>

        <label for="nivelAcesso">Nível de acesso:</label>
        <input type="number" id="nivelAcesso" name="nivelAcesso"  value="${cargoSelecionado.acesso}" required><br>

        <button type="submit">Salvar</button>
    </form>

</div>

</body>
</html>
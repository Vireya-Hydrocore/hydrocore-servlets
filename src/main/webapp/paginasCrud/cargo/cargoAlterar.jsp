<%--
  Created by IntelliJ IDEA.
  User: iagodiniz-ieg
  Date: 05/10/2025
  Time: 16:18
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Alterar Cargo</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<h1>Alterar Cargo</h1>

<form action="${pageContext.request.contextPath}/ServletCargo" method="post">
    <input type="hidden" name="action" value="updateCargo">
    <input type="hidden" name="id" value="${CargoSelecionado.id}">

    <label>Nome:</label>
    <input type="text" name="nome" value="${CargoSelecionado.nome}" required><br><br>

    <label>Acesso:</label>
    <input type="number" name="tipo" value="${CargoSelecionado.acesso}" required><br><br>

    <button type="submit">Salvar Alterações</button>
    <a href="${pageContext.request.contextPath}/ServletCargo?action=mainCargo">Cancelar</a>
</form>

</body>
</html>

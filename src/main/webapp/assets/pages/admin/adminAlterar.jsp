<%--
  Created by IntelliJ IDEA.
  User: eriksilva-ieg
  Date: 05/10/2025
  Time: 20:16
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.example.servletsvireya.dto.AdminDTO" %>

<%
    AdminDTO admin = (AdminDTO) request.getAttribute("admin");
%>

<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <title>Admin</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/styleAlterar.css">
</head>
<body>

<div class="form-container">
    <h2>Alterar Admin</h2>

    <form action="${pageContext.request.contextPath}/ServletAdmin" method="post">
        <input type="hidden" name="action" value="updateAdmin"> <!-- Servlet enxerga que o action é updateAdmin -->

        <div class="campos-readonly">
            <label>ID</label>
            <input type="number" name="id" value="${admin.id}" readonly>
        </div>

        <div class="campos">
            <label>Nome</label>
            <input type="text" name="nome" value="${admin.nome}" required>
        </div>

        <div class="campos">
            <label>E-mail</label>
            <input type="email" name="email" value="${admin.email}" required>
        </div>

        <!-- Nome ETA -->
        <div class="campos-readonly">
            <label>ETA</label>
            <input type="text" name="nomeEta" value="${admin.nomeEta}" readonly>
        </div>

        <div class="acoes">
            <input type="submit" value="Salvar Alterações">
        </div>
    </form>
</div>

</body>
</html>

<%--
  Created by IntelliJ IDEA.
  User: iagodiniz-ieg
  Date: 10/10/2025
  Time: 19:03
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html lang="pt-br">
<head>
  <meta charset="UTF-8">
  <title>Login Admin</title>
  <link rel="shortcut icon" href="${pageContext.request.contextPath}/assets/imgs/vireya_icon.png" type="image/x-icon">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/loginAdmin.css">
</head>

<style>
  body{
    /* várias imagens ao mesmo tempo */
    background-image:
    url("${pageContext.request.contextPath}/assets/imgs/vireya_icon.png"),   /* flor */
    url("${pageContext.request.contextPath}/assets/imgs/ondaAzul.png"),      /* onda esquerda */
    url("${pageContext.request.contextPath}/assets/imgs/ondaVerde.png");     /* onda direita */
  }
</style>

<body>

<div class="login-container">
  <h2>Login (Área restrita)</h2>

  <%
    String erro = (String) request.getAttribute("erroLogin");
    if (erro != null) {
  %>
  <div class="erro"><%= erro %></div>
  <% } %>

  <form action="${pageContext.request.contextPath}/ServletLogin?action=logarAdmin" method="post">
    <label for="email">Email:</label>
    <input type="email" id="email" name="email" placeholder="Digite seu email" required>

    <label for="senha">Senha:</label>
    <input type="password" id="senha" name="senha" placeholder="Digite sua senha" required>

    <button type="submit">Entrar</button>
  </form>
</div>
</body>
</html>
<%--
  Created by IntelliJ IDEA.
  User: iagodiniz-ieg
  Date: 06/10/2025
  Time: 21:10
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html lang="pt-br">
<head>
  <meta charset="UTF-8">
  <title>Página de Login</title>
  <style>
    body {
      font-family: Arial, sans-serif;
      background-color: #f0f2f5;
      display: flex;
      justify-content: center;
      align-items: center;
      height: 100vh;
    }
    .login-container {
      background-color: #fff;
      padding: 30px 40px;
      border-radius: 10px;
      box-shadow: 0 2px 10px rgba(0,0,0,0.1);
      width: 320px;
    }
    h2 {
      text-align: center;
      margin-bottom: 25px;
      color: #333;
    }
    input[type="email"], input[type="password"] {
      width: 100%;
      padding: 10px;
      margin: 10px 0 20px 0;
      border: 1px solid #ccc;
      border-radius: 5px;
    }
    button {
      width: 100%;
      padding: 10px;
      background-color: #2d89ef;
      color: #fff;
      border: none;
      border-radius: 5px;
      cursor: pointer;
      font-size: 16px;
    }
    button:hover {
      background-color: #1b5fa7;
    }
    .erro {
      color: red;
      text-align: center;
      margin-bottom: 10px;
    }
  </style>
</head>
<body>
<div class="login-container">
  <h2>Login</h2>

  <%
    String erro = (String) request.getAttribute("erroLogin");
    if (erro != null) {
  %>
  <div class="erro"><%= erro %></div>
  <% } %>

  <form action="${pageContext.request.contextPath}/ServletLogin?action=logar" method="post">
    <label for="email">Email:</label>
    <input type="email" id="email" name="email" placeholder="Digite seu email" required>

    <label for="senha">Senha:</label>
    <input type="password" id="senha" name="senha" placeholder="Digite sua senha" required>

    <button type="submit">Entrar</button>
  </form>
</div>
</body>
</html>

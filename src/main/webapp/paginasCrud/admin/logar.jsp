<%--
  Created by IntelliJ IDEA.
  User: eriksilva-ieg
  Date: 08/10/2025
  Time: 09:06
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!--
Arquivo: login.html
Descrição: Página de login simples que envia email e senha via POST para /login
-->
<!doctype html>
<html lang="pt-BR">
<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>Login - Sistema</title>
  <style>
    body { font-family: Arial, sans-serif; background:#f5f5f5; }
    .container { width:360px; margin:80px auto; background:#fff; padding:24px; border-radius:8px; box-shadow:0 6px 18px rgba(0,0,0,0.06); }
    h1 { font-size:20px; margin-bottom:12px; }
    label { display:block; margin-top:8px; font-size:13px; }
    input[type="email"], input[type="password"] { width:100%; padding:10px; margin-top:6px; box-sizing:border-box; border:1px solid #ccc; border-radius:4px; }
    button { margin-top:16px; width:100%; padding:10px; border:0; border-radius:6px; cursor:pointer; font-weight:600; }
    .btn-primary { background:#2b6cb0; color:white; }
    .error { color:#b00020; margin-top:8px; }
  </style>
</head>
<body>
<div class="container">
  <h1>Entrar</h1>
  <form id="loginForm" method="post" action="${pageContext.request.contextPath}/ServletAdmin?action=logar">
    <label for="email">E-mail</label>
    <input id="email" name="email" type="email" required autocomplete="email" placeholder="seu@exemplo.com">

    <label for="password">Senha</label>
    <input id="password" name="senha" type="password" required autocomplete="current-password"  placeholder="Senha123@">

    <button type="submit" class="btn-primary">Entrar</button>
    <div id="message" class="error" role="alert" aria-live="polite"></div>
  </form>
</div>

</body>
</html>


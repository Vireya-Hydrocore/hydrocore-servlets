<%--
  Created by IntelliJ IDEA.
  User: iagodiniz-ieg
  Date: 04/10/2025
  Time: 14:57
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="pt-br">
<head>
  <meta charset="UTF-8">
  <title>Lista de Produtos</title>
</head>
<body>

  <% Boolean alterado = (Boolean) request.getAttribute("alteradoSucesso"); %>
  <% if (alterado != null && alterado) { %>
<script>
  window.onload = function() {
    alert("✅ Produto alterado com sucesso!");
  }
</script>
<% } %>
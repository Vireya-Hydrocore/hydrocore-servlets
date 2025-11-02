<%--
  Created by IntelliJ IDEA.
  User: eriksilva-ieg
  Date: 31/10/2025
  Time: 10:29
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html lang="pt-br">

<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/styleErro.css">
  <link rel="shortcut icon" href="${pageContext.request.contextPath}/assets/imgs/vireya_icon.png" type="image/x-icon">
  <title>Erro</title>
</head>

<body>
<main id="principal">
  <header>
    <h1>Erro</h1>
  </header>

  <img src="${pageContext.request.contextPath}/assets/imgs/erro404.jpg">
  <ul class="lista-erros">
    <li>Oops, algo deu errado... A página que você procurava não pode ser encontrada.</li>
  </ul>

  <div class="botoes">
    <a href="${pageContext.request.contextPath}/index.jsp" class="botao-voltar  ">Sair</a>
    <a href="javascript: history.go(-1)" class="botao-voltar">Voltar à Página Anterior</a>
  </div>
</main>
</body>

</html>
<%--
  Created by IntelliJ IDEA.
  User: eriksilva-ieg
  Date: 06/10/2025
  Time: 16:56
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
    <title>Erro</title>
</head>

<body>
<main id="principal">
    <header>
        <h1>Erro de <b>Validação</b></h1>
    </header>

    <%
        // Recupera a lista de erros enviada pelos Servlets
        List<String> erros = (List<String>) request.getAttribute("erros");
        if (erros != null && !erros.isEmpty()) {
    %>
    <img src="${pageContext.request.contextPath}/assets/imgs/image_erro.png">
    <p>Problemas Encontrados: </p>
    <ul class="lista-erros">
        <% for (String erro : erros) { %>
        <li><%= erro %></li>
        <% } %>
    </ul>
    <%
    } else {
    %>
    <p>Houve algum erro inesperado no sistema.</p>
    <%
        }
    %>
    <div class="botoes">
        <a href="${pageContext.request.contextPath}/index.jsp" class="botao-voltar">Sair</a>
        <a href="javascript: history.go(-1)" class="botao-voltar">Voltar à Página Anterior</a>
    </div>
</main>
</body>

</html>

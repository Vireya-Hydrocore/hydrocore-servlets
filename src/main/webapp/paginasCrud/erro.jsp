<%--
  Created by IntelliJ IDEA.
  User: eriksilva-ieg
  Date: 06/10/2025
  Time: 16:56
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<html lang="pt-br">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/paginasCrud/css/styleErro.css">
    <title>Erro de Validação</title>
</head>

<body>
<main id="principal">
    <div>
        <h1>❌ Erro de Validação</h1>
    </div>

    <%
        // Recupera a lista de erros enviada pelo Servlet
        List<String> erros = (List<String>) request.getAttribute("erros");
        if (erros != null && !erros.isEmpty()) {
    %>
    <p>Os seguintes problemas foram encontrados nos dados enviados:</p>
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

    <form action="${pageContext.request.contextPath}/ServletEta?action=mainEta" method="get">
        <button type="submit" class="botao-voltar">Voltar à Página Principal</button>
    </form>
</main>

</body>
</html>

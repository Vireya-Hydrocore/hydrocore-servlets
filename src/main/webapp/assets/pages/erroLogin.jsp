<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<!doctype html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
<%--    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/styleErro.css">--%>
    <title>Erro</title>
</head>
<body>
    <h1>Erro</h1>
    <%
        List<String> erros = (List<String>) request.getAttribute("erros");
        if (erros != null) {
    %>
    <div class="alert alert-danger">
        <% for (int i=0; i < erros.size(); i++) { %>
        <ul>
            <li><%= erros.get(i) %></li>
        </ul>
        <% } %>
    </div>
    <%
    } else if (request.getAttribute("erros") != null) {
    %>
    <div class="alert alert-danger">
        <p><%= request.getAttribute("erros") %></p>
    </div>
    <% } %>
</body>
</html>

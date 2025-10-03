<%--
  Created by IntelliJ IDEA.
  User: eriksilva-ieg
  Date: 11/09/2025
  Time: 20:26
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.example.servletsvireya.model.Produto" %>
<%@ page import="java.util.List" %>
<%
    List<Produto> lista = (List<Produto>) request.getAttribute("produtos");
%>
<html>

<head>
    <title>Title</title>
    <style>
        table{
            align-items: center;
            background-color: darkblue;
            color: aliceblue;
        }
    </style>
</head>

<body>
    <h1>Tabela de produtos</h1>
    <a href="${pageContext.request.contextPath}/servlet-listar-produto">Atualizar</a>

    <br><br>
    <table border="1">
        <thead>
        <th>ID</th>
        <th>Nome</th>
        <th>Tipo</th>
        <th>Unidade de Medida</th>
        <th>Concentração</th>
        <th>Ações</th>
        </thead>
        <tbody>
        <% if (lista != null && !lista.isEmpty()) {
            for (Produto p : lista) { %>
        <tr>
            <td><%= p.getId() %></td>
            <td><%= p.getNome() %></td>
            <td><%= p.getTipo() %></td>
            <td><%= p.getUnidadeMedida() %></td>
            <td><%= p.getConcentracao() %></td>
            <td>
                <!-- Botão Editar -->
                <a href="${pageContext.request.contextPath}/servlet-alterar-produto?id=<%= p.getId() %>">Editar</a>
                &nbsp;|&nbsp;
                <!-- Botão Excluir -->
                <a href="${pageContext.request.contextPath}/servlet-remover-produto?id=<%= p.getId() %>"
                   onclick="return confirm('Tem certeza que deseja excluir este produto?');">
                    Excluir
                </a>
            </td>
        </tr>
        <% }
        } else { %>
        <tr>
            <td colspan="6">Nenhum produto encontrado!</td>
        </tr>
        <% } %>
        </tbody>
    </table>
</body>

</html>
<%@ page import="com.example.servletsvireya.dto.EstoqueDTO" %><%--
  Created by IntelliJ IDEA.
  User: eriksilva-ieg
  Date: 05/10/2025
  Time: 01:21
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
  EstoqueDTO estoque = (EstoqueDTO) request.getAttribute("estoque");
%>

<!DOCTYPE html>
<html lang="pt-BR">
<head>
  <meta charset="UTF-8">
  <title>Estoque</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/styleAlterar.css">
</head>
<body>

<div class="form-container">
  <h2>Alterar Estoque de ${estoque.nomeProduto}</h2>

  <form action="${pageContext.request.contextPath}/ServletEstoque" method="post">
    <input type="hidden" name="action" value="updateEstoque"> <!-- Servlet enxerga que o action é updateEstoque -->

    <div class="campos-readonly">
      <label>ID</label>
      <input type="number" name="id" value="${estoque.id}" readonly>
    </div>

    <div class="campos">
      <label>Quantidade</label>
      <input type="number" min="0" name="quantidade" value="${estoque.quantidade}">
    </div>

    <div class="campos">
      <label>Data de Validade</label>
      <%@ page import="java.text.SimpleDateFormat" %>
      <%
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); // formato aceito pelo input type=date
        String dataValidadeFormatada = "";
        if (estoque.getDataValidade() != null) {
          dataValidadeFormatada = sdf.format(estoque.getDataValidade()); // formata a data antiga
        }
      %>

      <input type="date" name="dataValidade" value="<%= dataValidadeFormatada %>">
    </div>

    <div class="campos">
      <label>Minímo possível estocado</label>
      <input type="number" name="minPossivelEstocado" value="${estoque.minPossivelEstocado}">
    </div>

    <div class="acoes">
      <input type="submit" value="Salvar Alterações">
    </div>
  </form>
</div>

</body>
</html>
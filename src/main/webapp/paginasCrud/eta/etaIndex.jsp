<%--
  Created by IntelliJ IDEA.
  User: iagodiniz-ieg
  Date: 07/10/2025
  Time: 17:56
  To change this template use File | Settings | File Templates.
--%>

<!-------------------- MENU ETA ----------------------->
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.example.servletsvireya.dto.EtaDTO" %>
<%
  List<EtaDTO> lista = (List<EtaDTO>) request.getAttribute("etas");
%>

<!DOCTYPE html>
<html lang="pt-BR">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>HydroCore - ETAS</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/paginasCrud/css/style.css">
</head>

<body>
<header>
  <button class="menu" id="menu-toggle">☰</button>
  <h1 class="logo">HydroCore</h1>
  <div class="avatar-container">
    <div class="avatar" id="avatar">
      <div class="dropdown" id="dropdown">
        <ul>
          <div>
            <form action="${pageContext.request.contextPath}/ServletAdmin" method="post">
              <input type="hidden" name="action" value="logout">
              <button class="logout" type="submit">
                <li><img src="${pageContext.request.contextPath}/paginasCrud/img/porta.png">Sair</li>
              </button>
            </form>
          </div>
        </ul>
      </div>
    </div>
  </div>
</header>

<aside class="sidebar" id="sidebar">
  <ul>
    <a href="${pageContext.request.contextPath}/ServletEta?action=mainEta">
      <li><img src="${pageContext.request.contextPath}/paginasCrud/img/imagem9.png"> ETAs</li>
    </a>
    <a href="${pageContext.request.contextPath}/ServletFuncionario?action=mainFuncionario">
      <li><img src="${pageContext.request.contextPath}/paginasCrud/img/image10.png"> Funcionarios</li>
    </a>
    <a href="${pageContext.request.contextPath}/ServletEstoque?action=mainEstoque">
      <li><img src="${pageContext.request.contextPath}/paginasCrud/img/image11.png"> Estoque</li>
    </a>
    <a href="${pageContext.request.contextPath}/ServletProduto?action=mainProduto">
      <li><img src="${pageContext.request.contextPath}/paginasCrud/img/image12.png"> Produtos</li>
    </a>
    <a href="${pageContext.request.contextPath}/ServletCargo?action=mainCargo">
      <li><img src="${pageContext.request.contextPath}/paginasCrud/img/image13.png"> Cargo</li>
    </a>
    <a href="${pageContext.request.contextPath}/ServletAdmin?action=mainAdmin">
      <li><img src="${pageContext.request.contextPath}/paginasCrud/img/icons8-admin-settings-male-16.png"> Admin</li>
    </a>
  </ul>
</aside>

<div class="plano-de-fundo">
  <main>

    <!-- LISTA DAS ETAS -->

    <section class="lista">
      <h2>Lista de Estacoes de Tratamento de Agua</h2>
      <table>
        <thead>
        <th>ID</th>
        <th>Nome</th>
        <th>Capacidade</th>
        <th>Telefone</th>
        <th>CNPJ</th>
        <th>Ações</th>
        </thead>
        <tbody>
        <% if (lista != null && !lista.isEmpty()) {
          for (int i=0; i < lista.size(); i++) { %>
        <tr>
          <td><%= lista.get(i).getId() %></td>
          <td><%= lista.get(i).getNome() %></td>
          <td><%= lista.get(i).getCapacidade() %></td>
          <td><%= lista.get(i).getTelefone() %></td>
          <td><%= lista.get(i).getCnpj() %></td>
          <td>
            <!-- Botão Editar -->
            <a class="botao-editar" href="${pageContext.request.contextPath}/ServletEta?action=selectEta&id=<%= lista.get(i).getId() %>">Editar</a>
            &nbsp;|&nbsp;
            <!-- Botão Excluir -->
            <form action="<%= request.getContextPath() %>/ServletEta" method="get" style="display:inline;">
              <input type="hidden" name="action" value="deleteEta">
              <input type="hidden" name="id" value="<%= lista.get(i).getId() %>">
              <button class="botao-excluir" type="submit" onclick="return confirm('Tem certeza que deseja excluir essa ETA?');">
                Excluir
              </button>
            </form>
          </td>
        </tr>
        <% }
        } else { %>
        <tr>
          <td colspan="6">Nenhuma ETA encontrada!</td>
        </tr>
        <% } %>
        </tbody>
      </table>
    </section>
  </main>
</div>
<!-- Script -->
<script src="${pageContext.request.contextPath}/paginasCrud/scripts/script.js"></script>
</body>
</html>
<%--
  Created by IntelliJ IDEA.
  User: iagodiniz-ieg
  Date: 07/10/2025
  Time: 17:56
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.example.servletsvireya.dto.EtaDTO" %>

<%
  EtaDTO eta = (EtaDTO) request.getAttribute("eta");
%>

<!DOCTYPE html>
<html lang="pt-BR">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>HydroCore - Informações da ETA</title>
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
      <li><img src="${pageContext.request.contextPath}/paginasCrud/img/imagem9.png"> Informações</li>
    </a>
    <a href="${pageContext.request.contextPath}/ServletFuncionario?action=mainFuncionario">
      <li><img src="${pageContext.request.contextPath}/paginasCrud/img/image10.png"> Funcionários</li>
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
    <section class="informacoes">
      <h2>Informações da ETA</h2>

      <div class="grid">
        <div class="box">
          <h3>Nome da ETA</h3>
          <p><%= eta != null ? eta.getNome() : "Não informado" %></p>
        </div>

        <div class="box">
          <h3>Capacidade</h3>
          <p><%= eta != null ? eta.getCapacidade() + " m³" : "Não informado" %></p>
        </div>

        <div class="box cheia">
          <h3>Endereço</h3>
          <p>
            <% if (eta != null) { %>
            <%= eta.getRua() %>, <%= eta.getNumero() %> - <%= eta.getBairro() %> -
            <%= eta.getCidade() %> - <%= eta.getEstado() %>, CEP <%= eta.getCep() %>
            <% } else { %>
            Não informado
            <% } %>
          </p>
        </div>

        <div class="box">
          <h3>CNPJ</h3>
          <p><%= eta != null ? eta.getCnpj() : "Não informado" %></p>
        </div>

        <div class="box-alterar">
          <form action="${pageContext.request.contextPath}/ServletEta" method="get">
            <input type="hidden" name="action" value="alterar">
            <button class="botao-salvar" type="submit">Alterar</button>
          </form>
        </div>
      </div>
    </section>
  </main>
</div>

<script src="${pageContext.request.contextPath}/paginasCrud/scripts/script.js"></script>
</body>
</html>

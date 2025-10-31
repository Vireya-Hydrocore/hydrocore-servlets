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
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/styleCrud.css">
</head>

<body class="eta-page">
<header>
  <button class="menu" id="menu-toggle">☰</button>
  <h1 class="logo">HydroCore</h1>

  <div class="logout-container">
    <form action="${pageContext.request.contextPath}/ServletLogin" method="post">
      <input type="hidden" name="action" value="logout">
      <a href="${pageContext.request.contextPath}/assets/pages/landingpage/loginAdmin.jsp" class="logout-btn" type="submit" title="Sair">Sair</a>
    </form>
  </div>
</header>

<aside class="sidebar" id="sidebar">
  <ul>
    <a href="${pageContext.request.contextPath}/ServletDashboard">
      <li><img src="${pageContext.request.contextPath}/assets/imgs/icons8-painel-de-controle-16.png"> Dashboard</li>
    </a>
    <a href="${pageContext.request.contextPath}/ServletEta?action=mainEta">
      <li><img src="${pageContext.request.contextPath}/assets/imgs/imagem9.png"> ETAs</li>
    </a>
    <a href="${pageContext.request.contextPath}/ServletFuncionario?action=mainFuncionario">
      <li><img src="${pageContext.request.contextPath}/assets/imgs/image10.png"> Funcionários</li>
    </a>
    <a href="${pageContext.request.contextPath}/ServletEstoque?action=mainEstoque">
      <li><img src="${pageContext.request.contextPath}/assets/imgs/image11.png"> Estoque</li>
    </a>
    <a href="${pageContext.request.contextPath}/ServletProduto?action=mainProduto">
      <li><img src="${pageContext.request.contextPath}/assets/imgs/image12.png"> Produtos</li>
    </a>
    <a href="${pageContext.request.contextPath}/ServletCargo?action=mainCargo">
      <li><img src="${pageContext.request.contextPath}/assets/imgs/image13.png"> Cargos</li>
    </a>
    <a href="${pageContext.request.contextPath}/ServletAdmin?action=mainAdmin">
      <li><img src="${pageContext.request.contextPath}/assets/imgs/icons8-admin-settings-male-16.png"> Admins</li>
    </a>
  </ul>
</aside>

<div class="plano-de-fundo">
  <main>

    <!-- LISTA DAS ETAS -->

    <section class="lista">

      <!-- FILTRO -->
      <section class="filtro">
        <div class="filtro-titulo">
          <h2>Lista de ETAs</h2>
        </div>

        <form action="${pageContext.request.contextPath}/ServletEta" method="get" class="filtro-form">
          <input type="hidden" name="action" value="filtroEta">

          <div class="campos">
            <label>Coluna</label>
            <select name="nome_coluna">
              <option value="nome">Nome</option>
              <option value="capacidade">Capacidade</option>
              <option value="telefone">Telefone</option>
              <option value="cnpj">CNPJ</option>
              <option value="cep">CEP</option>
            </select>
          </div>

          <div class="campos">
            <label>Pesquisa</label>
            <input type="text" name="pesquisa" placeholder="Digite o termo de busca...">
          </div>

          <div class="acoes">
            <a class="botao-redefinir" style="text-decoration: none"
               href="${pageContext.request.contextPath}/ServletEta?action=mainEta">Redefinir filtragem</a>
            <button type="submit" class="botao-salvar">Aplicar Filtro</button>
          </div>
        </form>
      </section>

      <!-- TABELA DENTRO DO CONTAINER DE ROLAGEM -->
      <div class="tabela-container">
        <table>
          <thead>
          <tr>
            <th>ID</th>
            <th>Nome</th>
            <th>Capacidade</th>
            <th>Telefone</th>
            <th>CNPJ</th>
            <th>CEP</th>
            <th>Ações</th>
          </tr>
          </thead>
          <tbody>
          <% if (lista != null && !lista.isEmpty()) {
            for (int i = 0; i < lista.size(); i++) { %>
          <tr>
            <td><%= lista.get(i).getId() %></td>
            <td><%= lista.get(i).getNome() %></td>
            <td><%= lista.get(i).getCapacidade() %></td>
            <td><%= lista.get(i).getTelefone() %></td>
            <td><%= lista.get(i).getCnpj() %></td>
            <td><%= lista.get(i).getCep() %></td>

            <!-- Dados adicionais ocultos -->
            <td style="display: none"><%= lista.get(i).getRua() %></td>
            <td style="display: none"><%= lista.get(i).getBairro() %></td>
            <td style="display: none"><%= lista.get(i).getCidade() %></td>
            <td style="display: none"><%= lista.get(i).getEstado() %></td>
            <td style="display: none"><%= lista.get(i).getNumero() %></td>

            <td>
              <a class="botao-editar"
                 href="${pageContext.request.contextPath}/ServletEta?action=selectEta&id=<%= lista.get(i).getId() %>">Editar</a>
              &nbsp;|&nbsp;
              <form action="<%= request.getContextPath() %>/ServletEta" method="post" style="display:inline;">
                <input type="hidden" name="action" value="deleteEta">
                <input type="hidden" name="id" value="<%= lista.get(i).getId() %>">
                <button class="botao-excluir" type="submit"
                        onclick="return confirm('Tem certeza que deseja excluir essa ETA?');">
                  Excluir
                </button>
              </form>
            </td>
          </tr>
          <% }
          } else { %>
          <tr>
            <td colspan="7">Nenhuma ETA encontrada!</td>
          </tr>
          <% } %>
          </tbody>
        </table>
      </div>
    </section>
  </main>
</div>
<!-- Script -->
<script src="${pageContext.request.contextPath}/assets/js/script.js"></script>
</body>
</html>
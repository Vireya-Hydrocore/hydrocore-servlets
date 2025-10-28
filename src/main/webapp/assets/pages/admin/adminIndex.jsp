<%--
  Created by IntelliJ IDEA.
  User: eriksilva-ieg
  Date: 05/10/2025
  Time: 20:03
  To change this template use File | Settings | File Templates.
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.example.servletsvireya.dto.AdminDTO" %>
<%@ page import="com.example.servletsvireya.dto.EtaDTO" %>
<%@ page import="com.example.servletsvireya.dao.EtaDAO" %>
<%
  List<AdminDTO> lista = (List<AdminDTO>) request.getAttribute("admins");
%>
<!DOCTYPE html>
<html lang="pt-BR">

<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Admin</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/styleCrud.css">
</head>

<body>
<header>
  <button class="menu" id="menu-toggle">☰</button>
  <h1 class="logo">HydroCore</h1>

  <div class="logout-container">
    <form action="${pageContext.request.contextPath}/ServletAdmin" method="post">
      <input type="hidden" name="action" value="logout">
      <a href="${pageContext.request.contextPath}/assets/pages/landingpage/loginAdmin.jsp"
         class="logout-btn" type="submit" title="Sair">Sair</a>
    </form>
  </div>
</header>

<aside class="sidebar" id="sidebar">
  <ul>
    <a href="${pageContext.request.contextPath}/dashAnalise">
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

    <!-- CADASTRO DE ADMINS -->
    <section class="cadastro">
      <h2>Cadastro de Admins</h2>
      <form name="admin-form" action="${pageContext.request.contextPath}/ServletAdmin" method="post">
        <input type="hidden" name="action" value="createAdmin">

        <div class="campos">
          <label>Nome</label>
          <input type="text" name="nome" placeholder="Ex: Erik Andozia" required>
        </div>

        <div class="campos">
          <label>E-mail</label>
          <input type="email" name="email" placeholder="Ex: marcelogrilo@gmail.com" required>
        </div>

        <div class="campos">
          <label>Senha</label>
          <input type="password" name="senha" maxlength="30" placeholder="xxxxxxxxx" required>
        </div>

        <div class="campos">
          <label for="nomeEta">Nome da ETA</label>
          <select id="nomeEta" name="nomeEta">
            <option value="">Selecione uma ETA</option> <%-- Valor inicial vazio --%>
            <%-- Listando as ETAS disponíveis com etaDAO --%>
            <%
              EtaDAO etaDAO = new EtaDAO();
              List<EtaDTO> etaList = etaDAO.listarEtas();

              for (EtaDTO eta : etaList){%>
            <option value="<%= eta.getId() %>"> <%= eta.getNome() %> </option> <%-- Mostra o nome mas pega o id da ETA --%>
            <%}%>
          </select>
        </div>

        <div class="acoes">
          <input type="reset" class="botao-redefinir" value="Limpar">
          <input type="submit" class="botao-salvar" value="Salvar">
        </div>
      </form>
    </section>

    <!-- LISTA DE ADMINS -->
    <section class="lista">

      <!-- FILTRO DE ADMINS -->
      <div class="filtro">
        <div class="filtro-titulo">
          <h2>Lista de Admins</h2>
        </div>

        <form action="${pageContext.request.contextPath}/ServletAdmin" method="get" class="filtro-form">
          <input type="hidden" name="action" value="filtroAdmin">

          <div class="campos">
            <label>Coluna</label>
            <select name="nome_coluna">
              <option value="nome">Nome</option>
              <option value="email">E-mail</option>
              <option value="nome_eta">ETA</option>
            </select>
          </div>

          <div class="campos">
            <label>Pesquisa</label>
            <input type="text" name="pesquisa" placeholder="Digite o termo de busca...">
          </div>

          <div class="acoes filtro-acoes">
            <a class="botao-redefinir" style="text-decoration: none"
               href="${pageContext.request.contextPath}/ServletAdmin?action=mainAdmin">
              Redefinir filtragem
            </a>
            <button type="submit" class="botao-salvar">Aplicar Filtro</button>
          </div>
        </form>
      </div>

      <!-- LISTAGEM -->
      <div class="tabela-container">
        <table>
          <thead>
          <tr>
            <th>ID</th>
            <th>Nome</th>
            <th>Email</th>
            <th>ETA</th>
            <th>Ações</th>
          </tr>
          </thead>
          <tbody>
          <% if (lista != null && !lista.isEmpty()) {
            for (int i=0; i < lista.size(); i++) { %>
          <tr>
            <td><%= lista.get(i).getId() %></td>
            <td><%= lista.get(i).getNome() %></td>
            <td><%= lista.get(i).getEmail() %></td>
            <td><%= lista.get(i).getNomeEta() %></td>
            <td>
              <a class="botao-editar"
                 href="${pageContext.request.contextPath}/ServletAdmin?action=selectAdmin&id=<%= lista.get(i).getId() %>">
                Editar
              </a>
              &nbsp;|&nbsp;
              <form action="${pageContext.request.contextPath}/ServletAdmin"
                    method="post" style="display:inline;">
                <input type="hidden" name="action" value="deleteAdmin">
                <input type="hidden" name="id" value="<%= lista.get(i).getId() %>">
                <button class="botao-excluir" type="submit"
                        onclick="return confirm('Tem certeza que deseja excluir este admin?');">
                  Excluir
                </button>
              </form>
            </td>
          </tr>
          <% }
          } else { %>
          <tr>
            <td colspan="5">Nenhum admin encontrado!</td>
          </tr>
          <% } %>
          </tbody>
        </table>
      </div>
    </section>
  </main>
</div>

<script src="${pageContext.request.contextPath}/assets/js/script.js"></script>
</body>

</html>

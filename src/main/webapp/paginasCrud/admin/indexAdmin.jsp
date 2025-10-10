<%--
  Created by IntelliJ IDEA.
  User: eriksilva-ieg
  Date: 05/10/2025
  Time: 20:03
  To change this template use File | Settings | File Templates.
--%>

<!---------------------- MENU ADMIN ------------------------>
<%--
  Created by IntelliJ IDEA.
  User: eriksilva-ieg
  Date: 05/10/2025
  Time: 20:03
  To change this template use File | Settings | File Templates.
--%>

<!---------------------- MENU ADMIN ------------------------>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.example.servletsvireya.dto.AdminDTO" %>
<%
  List<AdminDTO> lista = (List<AdminDTO>) request.getAttribute("admins");
%>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Admin</title>
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
          <li>
            <form action="${pageContext.request.contextPath}/ServletAdmin" method="post">
              <input type="hidden" name="action" value="logout">
              <button class="logout" type="submit">
                <span><img src="${pageContext.request.contextPath}/paginasCrud/img/porta.png">Sair</span>
              </button>
            </form>
          </li>
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

    <!-- INSERÇÃO DE ESTOQUES -->
    <section class="cadastro">
      <h2>Cadastro de Admins</h2>
      <form name="frmAdmin" action="${pageContext.request.contextPath}/ServletAdmin" method="post" onsubmit="return validarAdmin();">
        <div class="campos">
          <input type="hidden" name="action" value="createAdmin">
          <label>Nome</label>
          <input type="text" name="nome" placeholder="Ex: Erik Andozia">
        </div>

        <div class="campos">
          <label>E-mail</label>
          <input type="email" name="email" placeholder="Ex: marcelogrilo@gmail.com">
        </div>

        <div class="campos">
          <label>Senha</label>
          <input type="password" name="senha" maxlength="30" placeholder="xxxxxxxxx">
        </div>

        <div class="campos">
          <label>ETA</label>
          <input type="text" name="nomeEta" maxlength="30" placeholder="ETA Guarau">
        </div>

        <div class="acoes">
          <button type="button" class="botao-cancelar">Cancelar</button>
          <input type="submit" value="Salvar" class="botao-salvar">
        </div>
      </form>
    </section>

    <!-- LISTA DE ADMINS -->
    <section class="lista">
      <h2>Lista de Admins</h2>
      <table>
        <thead>
        <th>ID</th>
        <th>Nome</th>
        <th>Email</th>
        <th>Senha</th>
        <th>ETA</th>
        <th>Ações</th>
        </thead>
        <tbody>
        <% if (lista != null && !lista.isEmpty()) {
          for (int i=0; i < lista.size(); i++) { %>
        <tr>
          <td><%= lista.get(i).getId() %></td>
          <td><%= lista.get(i).getNome() %></td>
          <td><%= lista.get(i).getEmail() %></td>
          <td><%= lista.get(i).getSenha() %></td>
          <td><%= lista.get(i).getNomeEta() %></td>
          <td>
            <a class="botao-editar" href="${pageContext.request.contextPath}/ServletAdmin?action=selectAdmin&id=<%= lista.get(i).getId() %>">Editar</a>
            &nbsp;|&nbsp;
            <form action="<%= request.getContextPath() %>/ServletAdmin" method="post" style="display:inline;">
              <input type="hidden" name="action" value="deleteAdmin">
              <input type="hidden" name="id" value="<%= lista.get(i).getId() %>">
              <button class="botao-excluir" type="submit" onclick="return confirm('Tem certeza que deseja excluir este admin?');">
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
    </section>
  </main>
</div>

<script src="${pageContext.request.contextPath}/paginasCrud/scripts/script.js"></script>
</body>
</html>

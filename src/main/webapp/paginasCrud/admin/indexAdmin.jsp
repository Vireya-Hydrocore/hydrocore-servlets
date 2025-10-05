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
  // Recupera a lista de admins do request, que deve ser enviada pelo servlet
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
  <div class="header-direito">
    <button class="area-restrita">Área Restrita</button>
    <button class="logout">Logout</button>
    <div class="avatar"></div>
  </div>
</header>

<aside class="sidebar" id="sidebar">
  <ul>
    <li><a href="/funcionarios.html">👨‍💼 Funcionários</a></li>
    <li><a href="/estoque.html">📦 Estoque</a></li>
    <li><a href="/produto.html">🧪 Produtos</a></li>
    <li><a href="/cargo.html">📋 Cargo</a></li>
  </ul>
</aside>

<div class="plano-de-fundo">
  <main>

    <!-- INSERÇÃO DE ESTOQUES -->

    <section class="cadastro">
      <h2>Cadastro de Admins</h2>
      <form name="frmProduto" action="${pageContext.request.contextPath}/ServletAdmin" method="post" onsubmit="return validar();">
        <div class="campos">
          <input type="hidden" name="action" value="createAdmin"> <!-- envia esse parâmetro para o servlet ver q é create-->

          <label>Nome</label>
          <input type="text" name="nome" placeholder="Ex: Erik Andozia">
        </div>

        <div class="campos">
          <label>E-mail</label>
          <input type="email" name="email" placeholder="Ex: marcelogrilo@gmail.com">
        </div>

        <div class="campos">
          <label>Senha</label>
          <input type="password" name="senha" placeholder="xxxxxxxxx">
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
          <td>
            <!-- Botão Editar -->
            <a class="botao-editar" href="${pageContext.request.contextPath}/ServletAdmin?action=selectAdmin&id=<%= lista.get(i).getId() %>">Editar</a>

            &nbsp;|&nbsp;

            <!-- Botão Excluir -->
            <form action="<%= request.getContextPath() %>/ServletAdmin" method="get" style="display:inline;">
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
          <td colspan="6">Nenhum produto encontrado!</td>
        </tr>
        <% } %>
        </tbody>
      </table>
    </section>
  </main>
</div>

<script>
  const menuBtn = document.getElementById("menu-toggle");
  const sidebar = document.getElementById("sidebar");

  menuBtn.addEventListener("click", () => {
    sidebar.classList.toggle("open");
  });
</script>
</body>
</html>
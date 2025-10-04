<%--
  Created by IntelliJ IDEA.
  User: eriksilva-ieg
  Date: 09/09/2025
  Time: 20:57
  To change this template use File | Settings | File Templates.
--%>
<!-------------------- MENU PRODUTO ----------------------->
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.example.servletsvireya.model.Produto" %>
<%@ page import="java.util.List" %>
<%
    List<Produto> lista = (List<Produto>) request.getAttribute("produtos");
%>
<!DOCTYPE html>
<html lang="pt-BR">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Produtos</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/paginasCrud/css/style.css">
</head>

<body>
  <header>
      <button class="menu" id="menu-toggle">☰</button>
      <h1 class="logo">HydroCore</h1>
      <div class="header-direito">
          <button class="area-restrita">Área Restrita</button>
          <button class="logout">Sair</button>
          <div class="avatar"></div>
      </div>
  </header>

  <aside class="sidebar" id="sidebar">
      <ul>
          <li><a href="/index.html">📊 Informações</a></li>
          <li><a href="/funcionarios.html">👨‍💼 Funcionarios</a></li>
          <li><a href="/estoque.html">📦 Estoque</a></li>
          <li><a href="/produto.html">🧪 Produtos</a></li>
          <li><a href="/cargo.html">📋 Cargo</a></li>
      </ul>
  </aside>
<div class="plano-de-fundo">
    <main>
        <!-- CADASTRO DE PRODUTOS -->
        <section class="cadastro">
            <h2>Cadastro de Produtos</h2>
            <form name="frmProduto" action="${pageContext.request.contextPath}/ServletProduto" method="post">
                <div class="campos">
                    <input type="hidden" name="action" value="create"> <!-- envia esse parametro para o servlet ver q é create-->

                    <label>Nome do Produto</label>
                    <input type="text" name="nome" placeholder="Ex: Cloro Líquido">
                </div>
                <div class="campos">
                    <label>Categoria</label>
                    <!--                        <select>-->
                    <!--                            <option>Floculante</option>-->
                    <!--                            <option>Coagulante</option>-->
                    <!--                            <option>Outro</option>-->
                    <!--                        </select>-->
                    <input type="text" name="tipo" placeholder="tipo">
                </div>
                <div class="campos">
                    <label>Unidade de Medida</label>
                    <input type="text" name="unidadeMedida" placeholder="Ex: mg/L">
                </div>
                <div class="campos">
                    <label>Concentração</label> <!-- Preço???????-->
                    <input type="number" name="concentracao" placeholder="Ex: 25.50">
                </div>
                <div class="acoes">
                    <button type="button" class="botao-cancelar">Cancelar</button>
                    <!--                        <button type="submit" class="botao-salvar">Salvar</button>-->
                    <input type="button" value="Salvar" class="botao-salvar" onclick="validar()">
                </div>
            </form>
        </section>

        <!-- LISTA DOS PRODUTOS -->

        <section class="lista">
            <h2>Lista de Produtos</h2>
            <table>
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
                    for (int i=0; i < lista.size(); i++) { %>
                <tr>
                    <td><%= lista.get(i).getId() %></td>
                    <td><%= lista.get(i).getNome() %></td>
                    <td><%= lista.get(i).getTipo() %></td>
                    <td><%= lista.get(i).getUnidadeMedida() %></td>
                    <td><%= lista.get(i).getConcentracao() %></td>
                    <td>
                        <!-- Botão Editar -->
                        <a class="botao-editar" href="${pageContext.request.contextPath}/ServletProduto?action=select&id=<%= lista.get(i).getId() %>">Editar</a>
                        &nbsp;|&nbsp;
                        <!-- Botão Excluir -->
                        <a class="botao-excluir" href="javascript: confirmar(<%= lista.get(i).getId() %>)">
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
        </section>
    </main>
</div>
<!-- Script -->
<script src="${pageContext.request.contextPath}/paginasCrud/scripts/validador.js"></script>
  <script src="${pageContext.request.contextPath}/paginasCrud/scripts/confirmador.js"></script>
<script>
    const menuBtn = document.getElementById("menu-toggle");
    const sidebar = document.getElementById("sidebar");

    menuBtn.addEventListener("click", () => {
        sidebar.classList.toggle("open");
    });
</script>
  
</body>
</html>

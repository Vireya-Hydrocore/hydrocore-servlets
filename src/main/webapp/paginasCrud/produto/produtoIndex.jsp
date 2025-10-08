<%--
  Created by IntelliJ IDEA.
  User: eriksilva-ieg
  Date: 09/09/2025
  Time: 20:57
  To change this template use File | Settings | File Templates.
--%>
<!-------------------- MENU PRODUTO ----------------------->
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.example.servletsvireya.dto.ProdutoDTO" %>
<%
    List<ProdutoDTO> lista = (List<ProdutoDTO>) request.getAttribute("produtos");
%>
<!DOCTYPE html>
<html lang="pt-BR">

<head>
    <meta charset="UTF-8">
    <title>Lista de Produtos</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/paginasCrud/css/styleProduto.css">
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
          <li><a href="${pageContext.request.contextPath}/ServletFuncionario?action=mainFuncionario">👨‍💼 Funcionários</a></li>
          <li><a href="${pageContext.request.contextPath}/ServletEstoque?action=mainEstoque">📦 Estoque</a></li>
          <li class="ativo"><a href="${pageContext.request.contextPath}/ServletProduto?action=mainProduto">🧪 Produtos</a></li>
          <li><a href="${pageContext.request.contextPath}/ServletCargo?action=mainCargo">📋 Cargo</a></li>
      </ul>
  </aside>
<div class="plano-de-fundo">
    <main>
        <!-- CADASTRO DE PRODUTOS -->
        <section class="cadastro">
            <h2>Cadastro de Produtos</h2>
            <form name="frmProduto" action="${pageContext.request.contextPath}/ServletProduto" method="post" onsubmit="return validarProduto();">
                <div class="campos">
                    <input type="hidden" name="action" value="createProduto"> <!-- Envia esse parametro para o servlet ver que action é create-->

                    <label>Nome do Produto</label>
                    <input type="text" name="nome" placeholder="Ex: Cloro Líquido">
                </div>

                <div class="campos">
                    <label>Tipo</label>
                    <select name="tipo">
                        <option value="">Selecione a categoria</option>
                        <option value="Coagulante">Coagulante</option>
                        <option value="Floculante">Floculante</option>
                    </select>
                </div>

                <div class="campos">
                    <label>Unidade de Medida</label>
                    <select name="unidadeMedida">
                        <option value="">Selecione a unidade</option>
                        <option value="kg">kg</option>
                        <option value="g">g</option>
                        <option value="L">L</option>
                        <option value="mL">mL</option>
                        <option value="mg/L">mg/L</option>
                        <option value="µg/L">µg/L</option>
                    </select>
                </div>

                <div class="campos">
                    <label>Concentração (%)</label>
                    <input type="number" name="concentracao" max="100" placeholder="Ex: 25.50">
                </div>

                <div class="acoes">
                    <button type="button" class="botao-cancelar">Cancelar</button>

                    <input type="submit" value="Salvar" class="botao-salvar">
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
                        <a class="botao-editar" href="${pageContext.request.contextPath}/ServletProduto?action=selectProduto&id=<%= lista.get(i).getId() %>">Editar</a>
                        &nbsp;|&nbsp;
                        <!-- Botão Excluir -->
                        <form action="<%= request.getContextPath() %>/ServletProduto" method="get" style="display:inline;">
                            <input type="hidden" name="action" value="deleteProduto">
                            <input type="hidden" name="id" value="<%= lista.get(i).getId() %>">
                            <button class="botao-excluir" type="submit" onclick="return confirm('Tem certeza que deseja excluir este produto?');">
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
<!-- Script -->
<script src="${pageContext.request.contextPath}/paginasCrud/scripts/validador.js"></script>
<script>
    const menuBtn = document.getElementById("menu-toggle");
    const sidebar = document.getElementById("sidebar");

    menuBtn.addEventListener("click", () => {
        sidebar.classList.toggle("open");
    });
</script>

</body>
</html>

<%--
  Created by IntelliJ IDEA.
  User: iagodiniz-ieg
  Date: 05/10/2025
  Time: 16:18
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.example.servletsvireya.dto.CargoDTO" %>

<%
    List<CargoDTO> lista = (List<CargoDTO>) request.getAttribute("cargo");
    Boolean alterado = (Boolean) request.getAttribute("alteradoSucesso");
%>

<!DOCTYPE html>
<html lang="pt-BR">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Cargos</title>
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
        <li><a href="${pageContext.request.contextPath}/ServletProduto?action=mainProduto">🧪 Produtos</a></li>
        <li class="ativo"><a href="${pageContext.request.contextPath}/ServletCargo?action=mainCargo">📋 Cargo</a></li>
    </ul>
</aside>

<div class="plano-de-fundo">
    <main>

        <!-- CADASTRO DE CARGOS -->
        <section class="cadastro">
            <h2>Cadastro de Cargos</h2>
            <form name="frmCargo" action="${pageContext.request.contextPath}/ServletCargo" method="post">
                <div class="campos">
                    <input type="hidden" name="action" value="createCargo">

                    <label>Nome do Cargo</label>
                    <input type="text" name="nome" placeholder="Ex: Operador de ETA" required>
                </div>

                <div class="campos">
                    <label>Nível de Acesso</label>
                    <input type="number" name="acesso" placeholder="Ex: 1" required>
                </div>

                <div class="acoes">
                    <button type="button" class="botao-cancelar">Cancelar</button>
                    <input type="submit" value="Salvar" class="botao-salvar">
                </div>
            </form>
        </section>

        <!-- LISTA DE CARGOS -->
        <section class="lista">
            <h2>Lista de Cargos</h2>

            <% if (alterado != null && alterado) { %>
            <script>
                window.onload = function() {
                    alert("✅ Cargo alterado com sucesso!");
                }
            </script>
            <% } %>

            <table>
                <thead>
                <th>ID</th>
                <th>Nome</th>
                <th>Acesso</th>
                <th>Ações</th>
                </thead>
                <tbody>
                <% if (lista != null && !lista.isEmpty()) {
                    for (CargoDTO c : lista) { %>
                <tr>
                    <td><%= c.getId() %></td>
                    <td><%= c.getNome() %></td>
                    <td><%= c.getAcesso() %></td>
                    <td>
                        <!-- Editar -->
                        <a class="botao-editar"
                           href="${pageContext.request.contextPath}/ServletCargo?action=editarCargo&id=<%= c.getId() %>">Editar</a>

                        &nbsp;|&nbsp;

                        <!-- Excluir -->
                        <form action="<%= request.getContextPath() %>/ServletCargo" method="get" style="display:inline;">
                            <input type="hidden" name="action" value="deleteCargo">
                            <input type="hidden" name="id" value="<%= c.getId() %>">
                            <button class="botao-excluir" type="submit"
                                    onclick="return confirm('Tem certeza que deseja excluir este cargo?');">
                                Excluir
                            </button>
                        </form>
                    </td>
                </tr>
                <% }
                } else { %>
                <tr>
                    <td colspan="4">Nenhum cargo encontrado!</td>
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

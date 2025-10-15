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
    List<CargoDTO> lista = (List<CargoDTO>) request.getAttribute("cargos");
%>

<!DOCTYPE html>
<html lang="pt-BR">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Cargos</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/paginasCrud/css/style.css">
</head>

<body>
<header>
    <button class="menu" id="menu-toggle">☰</button>
    <h1 class="logo">HydroCore</h1>

    <div class="logout-container">
        <form action="${pageContext.request.contextPath}/ServletAdmin" method="post">
            <input type="hidden" name="action" value="logout">
            <a href="${pageContext.request.contextPath}/paginasCrud/admin/logarAdmin.jsp" class="logout-btn" type="submit" title="Sair">Sair</a>
        </form>
    </div>
</header>

<aside class="sidebar" id="sidebar">
    <ul>
        <a href="${pageContext.request.contextPath}/ServletEta?action=mainEta">
            <li><img src="${pageContext.request.contextPath}/paginasCrud/img/imagem9.png"> ETAs</li>
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

                <div class="campos">
                    <label>Nome ETA</label>
                    <input type="text" name="nomeEta" placeholder="Ex: ETA Guarau" required>
                </div>

                <div class="acoes">
                    <button type="button" class="botao-redefinir">Limpar</button>
                    <input type="submit" value="Salvar" class="botao-salvar">
                </div>
            </form>
        </section>

        <!-- LISTA DE CARGOS -->

        <section class="lista">

            <!-- FILTRO DE CARGOS -->
            <section class="filtro">
            <h2>Lista de Cargos</h2>

                <form action="${pageContext.request.contextPath}/ServletCargo" method="get">
                    <input type="hidden" name="action" value="filtroCargo">

                    <div class="campos">
                        <label>Coluna</label>
                        <select name="nome_coluna">
                            <option value="nome">Nome do Cargo</option>
                            <option value="acesso">Nível</option>
                            <option value="nome_eta">ETA</option>
                        </select>
                    </div>

                    <div class="campos">
                        <label>Pesquisa</label>
                        <input type="text" name="pesquisa" placeholder="Digite o termo de busca...">
                    </div>

                    <div class="acoes">
                        <a class="botao-redefinir" style="text-decoration: none" href="${pageContext.request.contextPath}/ServletCargo?action=mainCargo">Redefinir filtragem</a>
                        <button type="submit" class="botao-salvar">Aplicar Filtro</button>
                    </div>
                </form>
            </section>

<%--            <% if (alterado != null && alterado) { %>--%>
<%--            <script>--%>
<%--                window.onload = function() {--%>
<%--                    alert("✅ Cargo alterado com sucesso!");--%>
<%--                }--%>
<%--            </script>--%>
<%--            <% } %>--%>

            <table>
                <thead>
                <th>ID</th>
                <th>Nome</th>
                <th>Acesso</th>
                <th>ETA</th>
                <th>Ações</th>
                </thead>
                <tbody>
                <% if (lista != null && !lista.isEmpty()) {
                    for (CargoDTO c : lista) { %>
                <tr>
                    <td><%= c.getId() %></td>
                    <td><%= c.getNome() %></td>
                    <td><%= c.getAcesso() %></td>
                    <td><%= c.getNomeEta() %></td>
                    <td>
                        <!-- Editar -->
                        <a class="botao-editar"
                           href="${pageContext.request.contextPath}/ServletCargo?action=selectCargo&id=<%= c.getId() %>">Editar</a>

                        &nbsp;|&nbsp;

                        <!-- Excluir -->
                        <form action="<%= request.getContextPath() %>/ServletCargo" method="post" style="display:inline;">
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


<!-- POPUP DE EDIÇÃO -->
<div id="popupCargo" class="popup-overlay">
    <div class="popup-content">
        <h2>Alterar Cargo</h2>
        <form id="formEditarCargo" action="${pageContext.request.contextPath}/ServletCargo" method="post">
            <input type="hidden" name="action" value="updateCargo">
            <input type="hidden" id="idCargo" name="id">

            <label for="nomeCargo">Nome do Cargo:</label>
            <input type="text" id="nomeCargo" name="nome" required><br>

            <label for="nivelAcesso">Nível de Acesso:</label>
            <input type="number" id="nivelAcesso" name="acesso" required><br>

            <div class="acoes">
                <button type="button" id="btnFecharPopup" class="botao-redefinir">Cancelar</button>
                <button type="submit" class="botao-salvar">Salvar</button>
            </div>
        </form>
    </div>
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

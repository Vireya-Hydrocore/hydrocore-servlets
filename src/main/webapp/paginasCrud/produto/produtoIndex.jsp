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
<script src="${pageContext.request.contextPath}/paginasCrud/scripts/script.js"></script>
</body>
</html>
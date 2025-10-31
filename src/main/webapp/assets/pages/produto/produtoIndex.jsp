<%--
  Created by IntelliJ IDEA.
  User: eriksilva-ieg
  Date: 09/09/2025
  Time: 20:57
  To change this template use File | Settings | File Templates.
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.example.servletsvireya.dto.ProdutoDTO" %>
<%@ page import="com.example.servletsvireya.dao.EtaDAO" %>
<%@ page import="com.example.servletsvireya.dto.EtaDTO" %>
<%
    List<ProdutoDTO> lista = (List<ProdutoDTO>) request.getAttribute("produtos");
%>

<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>HydroCore - Informações da ETA</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/styleCrud.css">
</head>

<body>
<header>
    <button class="menu" id="menu-toggle">☰</button>
    <h1 class="logo">HydroCore</h1>

    <div class="logout-container">
        <form action="${pageContext.request.contextPath}/ServletAdmin" method="post">
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

        <!-- CADASTRO DE PRODUTOS -->

        <section class="cadastro">
            <h2>Cadastro de Produtos</h2>
            <form name="frmProduto" action="${pageContext.request.contextPath}/ServletProduto" method="post">
                <div class="campos">
                    <input type="hidden" name="action" value="createProduto"> <!-- Envia esse parametro para o servlet ver que action é create-->

                    <label>Nome do Produto</label>
                    <input type="text" name="nome" maxlength="50" placeholder="Ex: Cloro Líquido" required>
                </div>

                <div class="campos">
                    <label>Tipo</label>
                    <select name="tipo" required>
                        <option value="">Selecione a categoria</option>
                        <option value="Coagulante">Coagulante</option>
                        <option value="Floculante">Floculante</option>
                    </select>
                </div>

                <div class="campos">
                    <label>Unidade de Medida</label>
                    <select name="unidadeMedida" required>
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
                    <input type="number" name="concentracao" max="100" min="0" step="0.01" placeholder="Ex: 25.50" required>
                </div>

                <div class="campos">
                    <label for="nomeEta">Nome da ETA</label>
                    <select id="nomeEta" name="nomeEta">
                        <option value="">Selecione uma ETA</option> <%-- Valor inicial vazio --%>
                        <%-- Listando os cargos disponíveis com EtaDAO --%>
                        <%
                            EtaDAO etaDAO = new EtaDAO();
                            List<EtaDTO> etaList = etaDAO.listarEtas();

                            for (EtaDTO eta : etaList){%>
                        <option value="<%= eta.getId() %>"> <%= eta.getNome() %> </option> <%-- Mostra o nome e pega o id da ETA --%>
                        <%}%>
                    </select>
                </div>

                <div class="acoes">
                    <input type="reset" class="botao-redefinir" value="Limpar">
                    <input type="submit" value="Salvar" class="botao-salvar">
                </div>
            </form>
        </section>

        <!-- LISTA DOS PRODUTOS -->

        <section class="lista">

            <!-- FILTRO -->
            <section class="filtro">
                <div class="filtro-titulo">
                    <h2>Lista de Produtos</h2>
                </div>

                <form action="${pageContext.request.contextPath}/ServletProduto" method="get" class="filtro-form">
                    <input type="hidden" name="action" value="filtroProduto">

                    <div class="campos">
                        <label>Coluna</label>
                        <select name="nome_coluna">
                            <option value="nome">Nome</option>
                            <option value="tipo">Tipo</option>
                            <option value="unidade_medida">Unidade de Medida</option>
                            <option value="concentracao">Concentração</option>
                            <option value="nome_eta">ETA</option>
                        </select>
                    </div>

                    <div class="campos">
                        <label>Pesquisa</label>
                        <input type="text" name="pesquisa" placeholder="Digite o termo de busca...">
                    </div>

                    <div class="acoes">
                        <a class="botao-redefinir" style="text-decoration: none"
                           href="${pageContext.request.contextPath}/ServletProduto?action=mainProduto">Redefinir filtragem</a>
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
                        <th>Tipo</th>
                        <th>Unidade de Medida</th>
                        <th>Concentração</th>
                        <th>ETA</th>
                        <th>Ações</th>
                    </tr>
                    </thead>
                    <tbody>
                    <% if (lista != null && !lista.isEmpty()) {
                        for (ProdutoDTO p : lista) { %>
                    <tr>
                        <td><%= p.getId() %></td>
                        <td><%= p.getNome() %></td>
                        <td><%= p.getTipo() %></td>
                        <td><%= p.getUnidadeMedida() %></td>
                        <td><%= p.getConcentracao() %></td>
                        <td><%= p.getNomeEta() %></td>
                        <td>
                            <a class="botao-editar" href="${pageContext.request.contextPath}/ServletProduto?action=selectProduto&id=<%= p.getId() %>">Editar</a>
                            &nbsp;|&nbsp;
                            <form action="<%= request.getContextPath() %>/ServletProduto" method="post" style="display:inline;">
                                <input type="hidden" name="action" value="deleteProduto">
                                <input type="hidden" name="id" value="<%= p.getId() %>">
                                <button class="botao-excluir" type="submit"
                                        onclick="return confirm('Tem certeza que deseja excluir este produto?');">
                                    Excluir
                                </button>
                            </form>
                        </td>
                    </tr>
                    <% }
                    } else { %>
                    <tr>
                        <td colspan="7">Nenhum produto encontrado!</td>
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

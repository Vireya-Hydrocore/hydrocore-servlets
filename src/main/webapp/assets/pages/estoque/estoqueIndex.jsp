<%--
  Created by IntelliJ IDEA.
  User: eriksilva-ieg
  Date: 09/09/2025
  Time: 21:04
  To change this template use File | Settings | File Templates.
--%>
<!---------------------- MENU ESTOQUE ------------------------>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.example.servletsvireya.dto.EstoqueDTO" %>
<%
    // Recupera a lista de estoques do request, que deve ser enviada pelo servlet
    List<EstoqueDTO> lista = (List<EstoqueDTO>) request.getAttribute("estoques");

    Boolean alterado = (Boolean) request.getAttribute("alteradoSucesso");
%>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Estoque</title>
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
            <li><img src="${pageContext.request.contextPath}/assets/imgs/image13.png"> Cargo</li>
        </a>
        <a href="${pageContext.request.contextPath}/ServletAdmin?action=mainAdmin">
            <li><img src="${pageContext.request.contextPath}/assets/imgs/icons8-admin-settings-male-16.png"> Admin</li>
        </a>
        <a href="${pageContext.request.contextPath}/dashAnalise">
            <li><img src="${pageContext.request.contextPath}/assets/imgs/icons8-painel-de-controle-16.png"> DashBoard</li>
        </a>
    </ul>
</aside>

<div class="plano-de-fundo">
    <main>

        <!-- INSERÇÃO DE ESTOQUES -->

        <section class="cadastro">
            <h2>Cadastro de Estoque</h2>
            <form name="frmEstoque" action="${pageContext.request.contextPath}/ServletEstoque" method="post">
                <input type="hidden" name="action" value="createEstoque"> <!-- Envia esse parametro para o servlet ver que é create-->

                <div class="campos">
                    <label>Quantidade</label>
                    <input type="number" name="quantidade" placeholder="Ex: 50">
                </div>

                <div class="campos">
                    <label>Data de validade</label>
                    <input type="date" name="dataValidade">
                </div>

                <div class="campos">
                    <label>Minímo possível estocado</label>
                    <input type="number" name="minPossivelEstocado" placeholder="Ex: 10">
                </div>

                <div class="campos">
                    <label>Produto</label>
                    <input type="text" name="nomeProduto" placeholder="Ex: Sulfato de alumínio">
                </div>

                <div class="campos">
                    <label>Nome Eta</label>
                    <input type="text" name="nomeEta" placeholder="Ex: ETA Central">
                </div>

                <div class="acoes">
                    <button type="reset" class="botao-redefinir">Limpar</button>
                    <input type="submit" value="Salvar" class="botao-salvar">
                </div>
            </form>
        </section>

        <!-- LISTA DE ESTOQUES -->

        <section class="lista">

            <section class="filtro">
            <h2>Lista dos estoques de produtos</h2>

                <form action="${pageContext.request.contextPath}/ServletEstoque" method="get">
                    <input type="hidden" name="action" value="filtroEstoque">

                    <div class="campos">
                        <label>Coluna</label>
                        <select name="nome_coluna" id="colunaSelect">
                            <option value="nome_produto">Nome do Produto</option>
                            <option value="quantidade">Quantidade</option>
                            <option value="data_validade">Data de Validade</option>
                            <option value="min_possivel_estocado">Mínimo Possível Estocado</option>
                            <option value="nome_eta">ETA</option>
                        </select>
                    </div>

                    <div class="campos">
                        <label>Pesquisa</label>
                        <input type="text" name="pesquisa" id="pesquisa" placeholder="Digite o termo de busca...">
                    </div>

                    <div class="acoes">
                        <a class="botao-redefinir" style="text-decoration: none" href="${pageContext.request.contextPath}/ServletEstoque?action=mainEstoque">Redefinir filtragem</a>
                        <button type="submit" class="botao-salvar">Aplicar Filtro</button>
                    </div>
                </form>
                <script>

                    const colunaSelect = document.getElementById('colunaSelect');
                    const inputPesquisa = document.getElementById('pesquisa');

                    colunaSelect.addEventListener('change', function() {
                        const valor = colunaSelect.value;

                        // Se for uma das opções de data, muda o tipo para "date"
                        if (valor === 'data_validade') {
                            inputPesquisa.type = 'date';
                            inputPesquisa.value = ''; // limpa o campo para evitar erro de formato
                        } else {
                            inputPesquisa.type = 'text';
                            inputPesquisa.value = '';
                        }
                    });
                </script>
            </section>

            <table>
                <thead>
                <th>ID</th>
                <th>Nome Produto</th>
                <th>Quantidade</th>
                <th>Data Validade</th>
                <th>Minímo Possível Estocado</th>
                <th>ETA</th>
                <th>Ações</th>
                </thead>
                <tbody>
                <% if (lista != null && !lista.isEmpty()) {
                    for (int i=0; i < lista.size(); i++) { %>
                <tr>
                    <td><%= lista.get(i).getId() %></td>
                    <td><%= lista.get(i).getNomeProduto() %></td>
                    <td><%= lista.get(i).getQuantidade() %></td>
                    <td><%= lista.get(i).getDataValidade() %></td>
                    <td><%= lista.get(i).getMinPossivelEstocado() %></td>
                    <td><%= lista.get(i).getNomeEta() %></td>
                    <td>
                        <!-- Botão Editar -->
                        <a class="botao-editar" href="${pageContext.request.contextPath}/ServletEstoque?action=selectEstoque&id=<%= lista.get(i).getId() %>">Editar</a>

                        &nbsp;|&nbsp;

                        <!-- Botão Excluir -->
                        <form action="${pageContext.request.contextPath}/ServletEstoque" method="post" style="display:inline;">
                            <input type="hidden" name="action" value="deleteEstoque">
                            <input type="hidden" name="id" value="<%= lista.get(i).getId() %>">
                            <button class="botao-excluir" type="submit" onclick="return confirm('Tem certeza que deseja excluir este estoque?');">
                                Excluir
                            </button>
                        </form>
                    </td>
                </tr>
                <% }
                } else { %>
                <tr>
                    <td colspan="6">Nenhum estoque de produto encontrado!</td>
                </tr>
                <% } %>
                </tbody>
            </table>
        </section>
    </main>
</div>
<script src="${pageContext.request.contextPath}/assets/js/script.js"></script>
</body>

</html>

<%--
  Created by IntelliJ IDEA.
  User: eriksilva-ieg
  Date: 04/10/2025
  Time: 23:01
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.example.servletsvireya.dto.FuncionarioDTO" %>
<%
    // Recupera a lista de funcionarios do request, que deve ser enviada pelo servlet
    List<FuncionarioDTO> lista = (List<FuncionarioDTO>) request.getAttribute("funcionarios");

    Boolean alterado = (Boolean) request.getAttribute("alteradoSucesso");
%>
<!DOCTYPE html>
<html lang="pt-BR">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Funcionários</title>
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

        <!-- CADASTRO DE FUNCIONÁRIOS -->

        <section class="cadastro">
            <h2>Cadastro de Funcionários</h2>
            <form name="frmFuncionario" action="${pageContext.request.contextPath}/ServletFuncionario" method="post">
                <div class="campos">
                    <input type="hidden" name="action" value="createFuncionario"> <!-- envia esse parametro para o servlet ver q é create-->

                    <label>Nome</label>
                    <input type="text" name="nome" placeholder="Ex: Iago Eiken" required>
                </div>

                <div class="campos">
                    <label>E-mail</label>
                    <input type="text" name="email" placeholder="exemplo@gmail.com" required>
                </div>

                <div class="campos">
                    <label>Senha</label>
                    <input type="password" name="senha" placeholder="xxxxxxxxxxx" required>
                </div>

                <div class="campos">
                    <label>Data de Nascimento</label>
                    <input type="date" name="dataNascimento" required>
                </div>

                <div class="campos">
                    <label>Data de Admissão</label>
                    <input type="date" name="dataAdmissao" required>
                </div>

                <div class="campos">
                    <label>Cargo</label>
                    <input type="text" name="cargo" placeholder="Ex: Operador" required>
                </div>

                <div class="campos">
                    <label>Nome ETA</label>
                    <input type="text" name="nomeEta" placeholder="Ex: ETA Guarau" required>
                </div>

                <div class="acoes">
                    <button type="reset" class="botao-redefinir">Limpar</button>
                    <input type="submit" value="Salvar" class="botao-salvar">
                </div>
            </form>
        </section>

        <!-- LISTA DE FUNCIONARIOS -->

        <section class="lista">

            <div class="filtro">
                <div class="filtro-titulo">
                    <h2>Lista de Funcionários</h2>
                </div>

                <!-- FILTRO DE FUNCIONÁRIOS -->
                <form class="filtro-form" action="${pageContext.request.contextPath}/ServletFuncionario" method="get">
                    <input type="hidden" name="action" value="filtroFuncionario">

                    <div class="campos">
                        <label>Coluna</label>
                        <select name="nome_coluna" id="colunaSelect">
                            <option value="nome">Nome</option>
                            <option value="email">E-mail</option>
                            <option value="data_admissao">Data de Admissão</option>
                            <option value="data_nascimento">Data de Nascimento</option>
                            <option value="nome_cargo">Cargo</option>
                            <option value="nome_eta">ETA</option>
                        </select>
                    </div>

                    <div class="campos">
                        <label>Pesquisa</label>
                        <input type="text" id="pesquisa" name="pesquisa">
                    </div>

                    <div class="acoes filtro-acoes">
                        <a class="botao-redefinir" style="text-decoration: none" href="${pageContext.request.contextPath}/ServletFuncionario?action=mainFuncionario">Redefinir filtragem</a>
                        <button type="submit" class="botao-salvar">Aplicar Filtro</button>
                    </div>
                </form>

                <script>
                    const colunaSelect = document.getElementById('colunaSelect');
                    const inputPesquisa = document.getElementById('pesquisa');

                    colunaSelect.addEventListener('change', function() {
                        const valor = colunaSelect.value;

                        // Se for uma das opções de data, muda o tipo para "date"
                        if (valor === 'data_admissao' || valor === 'data_nascimento') {
                            inputPesquisa.type = 'date';
                            inputPesquisa.value = ''; // limpa o campo para evitar erro de formato
                        } else {
                            inputPesquisa.type = 'text';
                            inputPesquisa.value = '';
                        }
                    });
                </script>

            </div>

            <table>
                <thead>
                <th>ID</th>
                <th>Nome</th>
                <th>E-mail</th>
                <th>Data Admissão</th>
                <th>Data Nascimento</th>
                <th>Cargo</th>
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
                    <td><%= lista.get(i).getDataAdmissao()%></td>
                    <td><%= lista.get(i).getDataNascimento()%></td>
                    <td><%= lista.get(i).getNomeCargo()%></td>
                    <td><%= lista.get(i).getNomeEta()%></td>
                    <td>
                        <!-- Botão Editar -->
                        <a class="botao-editar" href="${pageContext.request.contextPath}/ServletFuncionario?action=selectFuncionario&id=<%= lista.get(i).getId() %>">Editar</a>
                        &nbsp;|&nbsp;
                        <!-- Botão Excluir -->
                        <form action="<%= request.getContextPath() %>/ServletFuncionario" method="post" style="display:inline;">
                            <input type="hidden" name="action" value="deleteFuncionario">
                            <input type="hidden" name="id" value="<%= lista.get(i).getId() %>">
                            <button class="botao-excluir" type="submit" onclick="return confirm('Tem certeza que deseja excluir este funcionário?');">
                                Excluir
                            </button>
                        </form>
                    </td>
                </tr>
                <% }
                } else { %>
                <tr>
                    <td colspan="6">Nenhum funcionário encontrado!</td>
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
